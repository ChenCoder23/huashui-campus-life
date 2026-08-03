package com.huashui.auth.controller;

import cn.dev33.satoken.stp.StpUtil;
import com.huashui.auth.domain.dto.LoginDTO;
import com.huashui.auth.domain.vo.CaptchaVO;
import com.huashui.auth.domain.vo.LoginVO;
import com.huashui.auth.service.authService;
import com.huashui.common.exception.BusinessException;
import com.huashui.common.response.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author
 */

@Tag(name = "登录中心", description = "登录、注册、注销、验证码")
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class loginController {


    /*
    * 可以用户的账号密码登录,
    * 绑定邮箱后可以邮箱加验证码登录
    */

    @Autowired
    private authService authService;


    @Operation(summary = "获取图形验证码")
    @GetMapping("/captcha")
    public Result<CaptchaVO> getCaptcha(HttpServletRequest request) {
        return authService.getCaptcha(request.getRemoteAddr());
    }


    @Operation(summary = "账密登录")
    @PostMapping("/login")
    public Result<LoginVO> userLogin(HttpServletRequest request, @RequestBody LoginDTO dto) {
        return authService.userLogin(dto , request.getRemoteAddr());
    }


    @Operation(summary = "退出登录")
    @PostMapping("/logout")
    public Result<Void> logout() {
        authService.logout();
        return Result.ok();
    }



/**
 * 发送绑定邮箱验证码验证码
 */
    @Operation(summary = "发送邮箱验证码")
    @PostMapping("/bind/email/send-code")
    public Result sendBindPhoneCode(@RequestParam String email){
        authService.setEmailCode(email);
        return Result.ok();
    }



     @Operation(summary = "邮箱验证码登录")
    @PostMapping("/email/login")
    public Result<LoginVO> login(@RequestParam @Email String email, @RequestParam @NotBlank String code) {

        return Result.ok(authService.EmailLogin(email,code));
    }

}
