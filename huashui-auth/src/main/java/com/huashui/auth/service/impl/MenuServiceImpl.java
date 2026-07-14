package com.huashui.auth.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 菜单服务实现类
 *
 * @author
 */
@Service
@RequiredArgsConstructor
public class MenuServiceImpl extends ServiceImpl<MenuMapper, Menu> implements MenuService {

    @Autowired
    private SysRoleMenuService roleMenuService;
    @Autowired
    private SysUserRoleService userRoleService;


    // 获取全量菜单树
    @Override
    public List<Menu> getMenuTree() {
        List<Menu> menuList = list();
        return MenuUtil.buildTree(menuList);
    }

    @Override
    public void addMenu(MenuDTO dto) {
        // todo 检查dto 属性的完整性  如果有父菜单,需要确定是否真实存在,检查是否有首页冲突 是否重复
        save(BeanUtil.copyProperties(dto,Menu.class));
    }

    @Override
    public void updateMenu(MenuDTO dto) {
        // 还需要删除原来的
        // todo 检查dto 属性的完整性  如果有父菜单,需要确定是否真实存在,检查是否有首页冲突
        save(BeanUtil.copyProperties(dto,Menu.class));
    }

    @Override
    public void dropMenu(Long id) {
        // todo 如果含有子菜单则无法删除
    }

    @Override
    public List<Menu> getMenuByRoleId(Long id) {

        List<SysRoleMenu> sysRoleMenus = roleMenuService.lambdaQuery().eq(SysRoleMenu::getRoleId, id).list();
        if (CollectionUtil.isEmpty(sysRoleMenus)){
            throw new BusinessException("还未未该角色分配菜单,请联系管理员");
        }
        List<Long> menuIds = sysRoleMenus.stream().map(SysRoleMenu::getMenuId).toList();
        List<Menu> menus = listByIds(menuIds); // todo 状态为禁用的不再返回
        return MenuUtil.buildTree(menus);

    }

    @Override
    public void setRolesMenu(Long id, List<Long> menuIds) {
        // todo

    }

    @Override
    public List<Menu> getMenuByUserId() {
        // 获取UserID
        Long userId = UserContext.getUserId();
        //查询用户对应的role
        SysUserRole role = userRoleService.lambdaQuery().eq(SysUserRole::getUserId, userId).one();

        if (role == null){
            throw new BusinessException("还未未该user分配角色,请联系管理员");
        }

        List<Menu> menus = getMenuByRoleId(role.getRoleId());
         return MenuUtil.buildTree(menus);
    }
}