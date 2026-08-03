package com.huashui.auth.controller;

import com.huashui.auth.domain.dto.BindEmailDTO;
import com.huashui.auth.domain.dto.PasswordDTO;
import com.huashui.auth.domain.pojo.Menu;
import com.huashui.auth.service.MenuService;
import com.huashui.auth.service.authService;
import com.huashui.common.response.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
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
@Tag(name = "个人权限中心")
public class profileController {

    @Autowired
    private MenuService menuService;
    @Autowired
    private authService authService;

    /**
     * 获取当前登录用户权限信息
     * <p>
     * 返回：
     * 该用户的菜单树菜单树
     */
    @GetMapping("/profile")
    @Operation(summary = "获取当前用户权限信息")
    public Result<List<Menu>> getProfile() {
        List<Menu> menuTree = menuService.getMenuByUserId();
        return Result.ok(menuTree);
    }


    /**
     * 修改密码
     * <p>
     * 原密码 -> 新密码
     */
    @PutMapping("/password")
    @Operation(summary = "修改密码")
    public Result<Void> updatePassword(@RequestBody PasswordDTO dto) {
        // todo 管理员修改不需要验证码但是学生和工作人员需要填写验证码
        authService.updatePassword(dto);
        return Result.ok();
    }

    /**
     * 修改头像
     * <p>
     * 前端先调 huashui-storage 上传获取 URL，再调此接口更新
     */
    @PutMapping("/profile/avatar")
    @Operation(summary = "修改头像")
    public Result<Void> updateAvatar(@RequestParam String avatarUrl) {
        authService.updateAvatar(avatarUrl);
        return Result.ok();
    }






    @PostMapping("/email")
    @Operation(summary = "绑定邮箱")
    public Result<Void> bindEmail(@RequestBody @Valid BindEmailDTO dto) {
        authService.bindEmail(dto);
        return Result.ok();
    }



    @PutMapping("/email")
    @Operation(summary = "更换邮箱")
    public Result<Void> updateEmail(@RequestBody BindEmailDTO dto) {

        authService.updateEmail(dto);

        return Result.ok();



    }


}
