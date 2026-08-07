package com.huashui.auth.controller;

import com.huashui.auth.domain.dto.RoleUpdateDTO;
import com.huashui.auth.domain.vo.RoleVO;
import com.huashui.auth.service.SysRoleService;
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




    private final SysRoleService roleService;



    /**
     * 查询用户角色
     */
    @GetMapping("/users/{id}/roles")
    @Operation(summary = "查询用户角色")
    public Result<RoleVO> getUserRoles(@PathVariable Long id) {

        return Result.ok(roleService.getUserRole(id));
    }

    /**
     * 设置用户拥有的角色
     *
     * 一个用户只能拥有一个角色
     */
    @PutMapping("/users/{id}/roles")
    @Operation(summary = "设置用户角色")
    public Result<Void> setUserRoles(
            @PathVariable Long id,
            @RequestBody Long roleId) {
        roleService.setUserRole(id, roleId);
        return Result.ok();
    }


    /**
     * 查询角色列表
     */
    @GetMapping("/roles")
    @Operation(summary = "查询角色列表")
    public Result<List<RoleVO>> getRoles() {

        return Result.ok(
                roleService.listRoles()
        );
    }


    /**
     * 修改角色信息
     *
     * 修改角色描述、状态等
     */
    @PutMapping("/roles/{id}")
    @Operation(summary = "修改角色")
    public Result<Void> updateRole(
            @PathVariable Long id,
            @RequestBody RoleUpdateDTO dto) {
        roleService.updateRole(id,dto);
        return Result.ok();
    }

}


