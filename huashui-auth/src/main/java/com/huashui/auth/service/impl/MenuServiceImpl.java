package com.huashui.auth.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.huashui.auth.domain.dto.MenuDTO;
import com.huashui.auth.domain.pojo.Menu;
import com.huashui.auth.mapper.MenuMapper;
import com.huashui.auth.service.MenuService;
import com.huashui.auth.util.menu.MenuUtil;
import lombok.RequiredArgsConstructor;
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


    // 获取全量菜单树
    @Override
    public List<Menu> getMenuTree() {
        List<Menu> menuList = list();
        return MenuUtil.buildTree(menuList);
    }

    @Override
    public void addMenu(MenuDTO dto) {

    }

    @Override
    public void updateMenu(Long id, Object dto) {

    }

    @Override
    public void dropMenu(Long id) {

    }

    @Override
    public List<Menu> getMenuByRoleId(Long id) {

        return null;
    }

    @Override
    public void setRolesMenu(Long id, List<Long> menuIds) {

    }
}