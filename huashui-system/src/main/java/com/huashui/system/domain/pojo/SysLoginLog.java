package com.huashui.system.domain.pojo;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("sys_login_log")
@Schema(description = "登录日志表")
public class SysLoginLog {

    @TableId(type = IdType.AUTO)
    private Long id;

    @Schema(description = "登录用户ID")
    private Long userId;

    @Schema(description = "登录用户名")
    private String username;

    @Schema(description = "登录时间")
    private LocalDateTime loginTime;

    @Schema(description = "登录IP")
    private String ipAddress;

    @Schema(description = "登录设备")
    private String device;

    @Schema(description = "浏览器")
    private String browser;

    @Schema(description = "操作系统")
    private String os;

    @Schema(description = "登录结果")
    private Integer status;

    @Schema(description = "失败原因")
    private String failReason;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;
}