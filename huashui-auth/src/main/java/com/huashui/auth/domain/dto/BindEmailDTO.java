package com.huashui.auth.domain.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * @author
 */
@Data
public class BindEmailDTO {

    @Schema(description = "邮箱地址", example = "example@qq.com")
    @NotBlank(message = "邮箱不能为空")
    @Email(message = "邮箱格式不正确")
    private String email;


    @Schema(description = "邮箱验证码", example = "123456")
    @NotBlank(message = "验证码不能为空")
    private String code;

}
