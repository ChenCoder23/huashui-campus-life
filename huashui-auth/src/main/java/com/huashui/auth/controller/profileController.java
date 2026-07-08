package com.huashui.auth.controller;

import com.huashui.auth.domain.dto.PasswordDTO;
import com.huashui.common.response.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * @author
 */

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Tag(name = "个人权限中心")
public class profileController {







        /**
         * 获取当前登录用户权限信息
         *
         * 返回：
         * 该用户的菜单树菜单树
         */
        @GetMapping("/profile")
        @Operation(summary = "获取当前用户权限信息")
        public Result<?> getProfile() {

            return Result.ok();
        }


        /**
         * 修改密码
         *
         * 原密码 -> 新密码
         */
        @PutMapping("/password")
        @Operation(summary = "修改密码")
        public Result<Void> updatePassword(@RequestBody PasswordDTO dto) {

            // todo 管理员修改不需要验证码但是学生和工作人员需要填写验证码
            return Result.ok();
        }

    }

