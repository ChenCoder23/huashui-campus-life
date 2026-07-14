package com.huashui.auth.domain.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * 修改密码DTO
 *
 * @author
 */
@Data
@Schema(description = "修改密码DTO")
public class PasswordDTO {

    @NotBlank(message = "账号不能为空")
    @Schema(description = "账号")
    private String account;


    @Schema(description = "旧密码")
    private String oldPassword;

    @NotBlank(message = "新密码不能为空")
    @Schema(description = "新密码")
    private String newPassword;

    @NotBlank(message = "确认密码不能为空")
    @Schema(description = "确认密码")
    private String confirmPassword;


    @Schema(description = "验证码Key")
    private String captchaKey;


    @Schema(description = "验证码")
    private String captchaCode;

}