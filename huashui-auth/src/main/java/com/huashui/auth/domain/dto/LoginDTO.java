package com.huashui.auth.domain.dto;

import com.huashui.common.enums.LoginType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
@Schema(description = "登录请求")
public class LoginDTO {

    @Schema(description = "账号/手机号/邮箱", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "账号不能为空")
    private String account;

    @Schema(description = "密码", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "密码不能为空")
    private String password;

    @Schema(description = "验证码key")
    private String captchaKey;

    @Schema(description = "验证码")
    private String captchaCode;

    @Schema(description = "登录类型")
    private LoginType loginType;


}
