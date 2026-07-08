package com.huashui.auth.controller;

import com.huashui.auth.domain.dto.MenuDTO;
import com.huashui.auth.domain.pojo.Menu;
import com.huashui.auth.service.MenuService;
import com.huashui.common.response.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author
 */

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Tag(name = "菜单管理中心")
public class menuController {


    @Autowired
    private MenuService menuService;

    /**
     * 获取全量菜单树（供超级管理员配置权限）
     */
    @GetMapping("/menus")
    @Operation(summary = "获取全量菜单树")
    public Result<List<Menu>> getMenuTree() {
        List<Menu> menuTree = menuService.getMenuTree();
        return Result.ok(menuTree);
    }

    /**
     * 新增菜单
     */
    @PostMapping("/menus")
    @Operation(summary = "新增菜单")
    public Result<Void> addMenu(@RequestBody MenuDTO dto) {
        menuService.addMenu(dto);
        return Result.ok();
    }

    /**
     * 修改菜单
     */
    @PutMapping("/menus")
    @Operation(summary = "修改菜单")
    public Result<Void> updateMenu(@RequestBody MenuDTO dto) {
        menuService.updateMenu(dto);
        return Result.ok();
    }

    /**
     * 删除菜单
     */
    @DeleteMapping("/menus/{id}")
    @Operation(summary = "删除菜单")
    public Result<Void> deleteMenu(@PathVariable Long id) {
        menuService.dropMenu(id);
        return Result.ok();
    }

    /**
     * 查询角色拥有的菜单权限
     */
    @GetMapping("/roles/{id}/menus")
    @Operation(summary = "查询角色菜单权限树")
    public Result<List<Menu>> getRoleMenus(@PathVariable Long id) {
        List<Menu> menuTree = menuService.getMenuByRoleId(id);
        return Result.ok(menuTree);
    }

    /**
     * 设置角色菜单权限
     */
    @PutMapping("/roles/{id}/menus")
    @Operation(summary = "设置角色菜单权限")
    public Result<Void> setRoleMenus(@PathVariable Long id, @RequestBody List<Long> menuIds) {
        // todo
        menuService.setRolesMenu(id,menuIds);
        return Result.ok();
    }



}
