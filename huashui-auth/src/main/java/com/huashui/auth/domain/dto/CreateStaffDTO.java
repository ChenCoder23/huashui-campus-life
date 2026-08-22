package com.huashui.auth.domain.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
@Schema(description = "新增工作人员用户")
public class CreateStaffDTO {

    @NotBlank(message = "账号不能为空")
    private String username;

    @NotBlank(message = "密码不能为空")
    private String password;

    @NotBlank(message = "姓名不能为空")
    private String realName;

    private String phone;

    private String email;

    @NotBlank(message = "角色编码不能为空")
    private String roleCode;

    private Integer gender;

    private Long campusId;

    private Long buildingId;

    private String major;

    private String grade;
}