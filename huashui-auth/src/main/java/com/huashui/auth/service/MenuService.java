package com.huashui.auth.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.huashui.auth.domain.dto.MenuDTO;
import com.huashui.auth.domain.pojo.Menu;

import java.util.List;

/**
 * 菜单服务
 *
 * @author
 */
public interface MenuService extends IService<Menu> {


    List<Menu> getMenuTree();

    void addMenu(MenuDTO dto);

    void updateMenu(MenuDTO dto);

    void dropMenu(Long id);

    List<Menu> getMenuByRoleId(Long id);

    void setRolesMenu(Long id, List<Long> menuIds);

    List<Menu> getMenuByUserId();
}