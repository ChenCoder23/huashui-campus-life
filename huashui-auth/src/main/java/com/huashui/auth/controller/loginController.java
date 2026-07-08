package com.huashui.auth.controller;

import com.huashui.auth.domain.dto.LoginDTO;
import com.huashui.auth.domain.vo.CaptchaVO;
import com.huashui.auth.domain.vo.LoginVO;
import com.huashui.auth.service.authService;
import com.huashui.common.response.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author
 */

@Tag(name = "认证中心", description = "登录、注册、注销、验证码")
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class loginController {

    @Autowired
    private authService authService;


    @Operation(summary = "获取图形验证码")
    @GetMapping("/captcha")
    public Result<CaptchaVO> getCaptcha() {
        return authService.getCaptcha();
    }



    @Operation(summary = "账密登录")
    @PostMapping("/login")
    public Result<LoginVO> userLogin(LoginDTO dto) {
        return authService.userLogin(dto);
    }

    @Operation(summary = "退出登录")
    @PostMapping("/logout")
    public Result<Void> logout() {
        authService.logout();
        return Result.ok();
    }

}
