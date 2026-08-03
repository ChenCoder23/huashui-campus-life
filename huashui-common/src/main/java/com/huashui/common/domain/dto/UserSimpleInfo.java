package com.huashui.common.domain.dto;

import com.huashui.common.enums.RoleCodeType;
import com.huashui.common.enums.Status;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 用户表
 *
 * @author
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "用户表")
public class UserSimpleInfo {

    @Schema(description = "主键ID")
    private Long id;

    @Schema(description = "用户名（学号/工号）")
    private String username;


    @Schema(description = "头像URL")
    private String avatar;

    @Schema(description = "密码（BCrypt加密）")
    private String password;

    @Schema(description = "真实姓名")
    private String realName;

    @Schema(description = "手机号")
    private String phone;

    @Schema(description = "邮箱")
    private String email;

    @Schema(description = "账号状态（0-冻结，1-正常）")
    private Status status;

    @Schema(description = "创建时间")
    private LocalDateTime createTime;

    @Schema(description = "更新时间")
    private LocalDateTime updateTime;

    @Schema(description = "角色编码")
    private RoleCodeType roleCode;

    @Schema(description = "最后登录时间")
    private LocalDateTime lastLoginTime;



}