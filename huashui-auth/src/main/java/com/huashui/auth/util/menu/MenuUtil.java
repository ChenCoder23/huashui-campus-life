package com.huashui.auth.util.menu;

import com.huashui.auth.domain.pojo.Menu;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author
 */
@Slf4j
public class MenuUtil {

    /**
     * 将扁平菜单列表转换为树形结构
     *
     * @param menus 扁平菜单列表
     * @return 菜单树
     */
    public static List<Menu> buildTree(List<Menu> menus) {

        // id -> Menu
        Map<Long, Menu> menuMap = new HashMap<>(menus.size());
        // 根节点
        List<Menu> roots = new ArrayList<>();

        // 第一次遍历：建立Map，同时初始化children
        for (Menu menu : menus) {
            menu.setChildren(new ArrayList<>());
            menuMap.put(menu.getId(), menu);
        }
        // 第二次遍历：组装树
        for (Menu menu : menus) {
            // 顶级菜单
            if (menu.getParentId() == 0L) {
                roots.add(menu);

            }else {
                Menu parent = menuMap.get(menu.getParentId());

                if (parent != null) {
                    parent.getChildren().add(menu);
                }

            }


        }


        return roots;
    }

    /**
     * 根据菜单id判断菜单是否存在
     *
     * @param menus 菜单列表
     * @param id    菜单id
     * @return true存在 false不存在
     */
    public static boolean existsById(List<Menu> menus, Long id) {
        if (menus == null || menus.isEmpty() || id == null) {
            return false;
        }

        return menus.stream()
                .anyMatch(menu -> id.equals(menu.getId()));
    }


    /**
     * 根据菜单id判断是否存在子菜单
     *
     * @param menus 菜单列表
     * @param id    菜单id
     * @return true存在子菜单 false不存在
     */
    public static boolean hasChildren(List<Menu> menus, Long id) {
        if (menus == null || menus.isEmpty() || id == null) {
            return false;
        }

        return menus.stream()
                .anyMatch(menu -> id.equals(menu.getParentId()));
    }



}
