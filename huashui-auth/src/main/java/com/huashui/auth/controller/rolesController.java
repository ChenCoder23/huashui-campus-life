package com.huashui.auth.controller;

import com.huashui.common.response.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Tag(name = "角色管理中心")
public class rolesController {


    @GetMapping("/users/{id}/roles")
    @Operation(summary = "查询用户角色")
    public Result<List<?>> getUserRoles(@PathVariable Long id) {

        return Result.ok();
    }

    /**
     * 设置用户拥有的角色
     */
    @PutMapping("/users/{id}/roles")
    @Operation(summary = "设置用户角色")
    public Result<Void> setUserRoles(@PathVariable Long id, @RequestBody List<Long> roleIds) {

        return Result.ok();
    }


    /**
     * 查询角色列表
     */
    @GetMapping("/roles")
    @Operation(summary = "查询角色列表")
    public Result<List<?>> getRoles() {

        return Result.ok();
    }


    /**
     * 新增角色
     */
    @PostMapping("/roles")
    @Operation(summary = "新增角色")
    public Result<Void> addRole(@RequestBody Object dto) {

        return Result.ok();
    }


    /**
     * 修改角色
     */
    @PutMapping("/roles/{id}")
    @Operation(summary = "修改角色")  // 修改角色描述和状态
    public Result<Void> updateRole(@PathVariable Long id, @RequestBody Object dto) {

        return Result.ok();
    }


    /**
     * 删除角色
     */
    @DeleteMapping("/roles/{id}")
    @Operation(summary = "删除角色")
    public Result<Void> deleteRole(@PathVariable Long id) {

        // todo 角色还有用户无法删除
        return Result.ok();
    }

}


