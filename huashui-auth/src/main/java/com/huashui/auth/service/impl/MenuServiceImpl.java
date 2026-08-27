package com.huashui.auth.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.huashui.auth.domain.dto.MenuDTO;
import com.huashui.auth.domain.pojo.Menu;
import com.huashui.auth.domain.pojo.SysRoleMenu;
import com.huashui.auth.domain.pojo.SysUserRole;
import com.huashui.auth.mapper.MenuMapper;
import com.huashui.auth.service.MenuService;
import com.huashui.auth.service.SysRoleMenuService;
import com.huashui.auth.service.SysUserRoleService;
import com.huashui.auth.util.menu.MenuUtil;
import com.huashui.common.exception.BusinessException;
import com.huashui.common.utils.UserContext;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.util.List;
import java.util.Set;
import java.util.UUID;

/**
 * 菜单服务实现类
 *
 * @author
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class MenuServiceImpl extends ServiceImpl<MenuMapper, Menu> implements MenuService {

    /** 全量菜单树缓存 key */
    private static final String MENU_ALL_CACHE_KEY = "auth:menus:all";

    /** 角色菜单树缓存 key 前缀 */
    private static final String ROLE_MENU_CACHE_PREFIX = "auth:menus:role:";

    /** 重建缓存锁的过期时间（兜底，防止宕机后锁不释放） */
    private static final Duration LOCK_TIMEOUT = Duration.ofSeconds(30);

    /** 未抢到锁的线程最长等待缓存重建的时间 */
    private static final long WAIT_TIMEOUT_MS = 10_000L;

    /** 未抢到锁的线程轮询缓存间隔 */
    private static final long POLL_INTERVAL_MS = 50L;

    @Autowired
    private SysRoleMenuService roleMenuService;

    @Autowired
    private SysUserRoleService userRoleService;

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    // 获取全量菜单树
    @Override
    public List<Menu> getMenuTree() {
        List<Menu> cached = readMenuCache(MENU_ALL_CACHE_KEY);
        if (cached != null) {
            return cached;
        }
        List<Menu> menuList = list();
        List<Menu> tree = MenuUtil.buildTree(menuList);
        writeMenuCache(MENU_ALL_CACHE_KEY, tree);
        return tree;
    }

    @Override
    public void addMenu(MenuDTO dto) {
        // todo 检查dto 属性的完整性  如果有父菜单,需要确定是否真实存在,检查是否有首页冲突 是否重复
        save(BeanUtil.copyProperties(dto, Menu.class));
        clearAllMenuCaches();
    }

    @Override
    public void updateMenu(MenuDTO dto) {
        // 还需要删除原来的
        // todo 检查dto 属性的完整性  如果有父菜单,需要确定是否真实存在,检查是否有首页冲突
        save(BeanUtil.copyProperties(dto, Menu.class));
        clearAllMenuCaches();
    }

    @Override
    public void dropMenu(Long id) {
        // todo 如果含有子菜单则无法删除
        clearAllMenuCaches();
    }

    @Override
    public List<Menu> getMenuByRoleId(Long id) {
        String cacheKey = ROLE_MENU_CACHE_PREFIX + id;

        // 1. 先查缓存，命中直接返回
        List<Menu> cached = readMenuCache(cacheKey);
        if (cached != null) {
            return cached;
        }

        // 2. 缓存未命中：加分布式锁，只允许一个线程查库重建，避免缓存击穿
        String lockKey = cacheKey + ":lock";
        String token = UUID.randomUUID().toString();
        if (tryAcquireLock(lockKey, token)) {
            try {
                // double-check：可能前一个持锁线程刚好重建完成
                List<Menu> again = readMenuCache(cacheKey);
                if (again != null) {
                    return again;
                }
                List<Menu> tree = loadRoleMenuTree(id);
                writeMenuCache(cacheKey, tree);
                return tree;
            } finally {
                releaseLock(lockKey, token);
            }
        }

        // 3. 未抢到锁：等待持锁线程重建完成，期间轮询缓存，避免无效的重复查库
        long deadline = System.currentTimeMillis() + WAIT_TIMEOUT_MS;
        while (System.currentTimeMillis() < deadline) {
            List<Menu> waited = readMenuCache(cacheKey);
            if (waited != null) {
                return waited;
            }
            sleepQuietly(POLL_INTERVAL_MS);
        }

        // 4. 超时兜底：直接查库返回（不再写缓存，避免覆盖）
        return loadRoleMenuTree(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void setRolesMenu(Long id, List<Long> menuIds) {
        // 1. 删除角色原有菜单关系
        roleMenuService.remove(
                new LambdaQueryWrapper<SysRoleMenu>()
                        .eq(SysRoleMenu::getRoleId, id));

        // 2. 重新建立角色-菜单关系
        if (CollectionUtil.isNotEmpty(menuIds)) {
            List<SysRoleMenu> relations = menuIds.stream()
                    .map(menuId -> {
                        SysRoleMenu relation = new SysRoleMenu();
                        relation.setRoleId(id);
                        relation.setMenuId(menuId);
                        return relation;
                    })
                    .toList();
            roleMenuService.saveBatch(relations);
        }

        // 3. 角色菜单发生修改，清空该角色的菜单树缓存
        clearRoleMenuCache(id);
    }

    @Override
    public List<Menu> getMenuByUserId() {
        // 获取UserID
        Long userId = UserContext.getUserId();

        //查询用户对应的role
        SysUserRole role = userRoleService.lambdaQuery().eq(SysUserRole::getUserId, userId).one();
        if (role == null) {
            throw new BusinessException("还未未该user分配角色,请联系管理员");
        }

        return getMenuByRoleId(role.getRoleId());
    }

    // ==================== 缓存与锁辅助方法 ====================

    /**
     * 查库并构建角色菜单树（未命中缓存的真正数据源）
     */
    private List<Menu> loadRoleMenuTree(Long roleId) {
        List<SysRoleMenu> sysRoleMenus = roleMenuService.lambdaQuery()
                .eq(SysRoleMenu::getRoleId, roleId)
                .list();
        if (CollectionUtil.isEmpty(sysRoleMenus)) {
            throw new BusinessException("还未未该角色分配菜单,请联系管理员");
        }
        List<Long> menuIds = sysRoleMenus.stream().map(SysRoleMenu::getMenuId).toList();
        List<Menu> menus = listByIds(menuIds); // todo 状态为禁用的不再返回

        return MenuUtil.buildTree(menus);
    }

    private List<Menu> readMenuCache(String key) {
        Object value = redisTemplate.opsForValue().get(key);
        if (value == null) {
            return null;
        }
        return JSONUtil.toList(value.toString(), Menu.class);
    }

    private void writeMenuCache(String key, List<Menu> tree) {
        redisTemplate.opsForValue().set(key, JSONUtil.toJsonStr(tree));
    }

    private boolean tryAcquireLock(String lockKey, String token) {
        Boolean ok = redisTemplate.opsForValue().setIfAbsent(lockKey, token, LOCK_TIMEOUT);
        return Boolean.TRUE.equals(ok);
    }

    private void releaseLock(String lockKey, String token) {
        // 只释放自己持有的锁，避免误删其他线程刚重建后的锁
        String current = redisTemplate.opsForValue().get(lockKey);
        if (token.equals(current)) {
            redisTemplate.delete(lockKey);
        }
    }

    private void clearRoleMenuCache(Long roleId) {
        redisTemplate.delete(ROLE_MENU_CACHE_PREFIX + roleId);
    }

    private void clearAllMenuCaches() {
        redisTemplate.delete(MENU_ALL_CACHE_KEY);
        Set<String> roleKeys = redisTemplate.keys(ROLE_MENU_CACHE_PREFIX + "*");
        if (CollectionUtil.isNotEmpty(roleKeys)) {
            redisTemplate.delete(roleKeys);
        }
    }

    private void sleepQuietly(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}