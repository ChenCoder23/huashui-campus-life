package com.huashui.auth.domain.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Schema(description = "管理员用户分页VO")
public class AdminUserVO {

    @Schema(description = "用户ID")
    private Long id;

    @Schema(description = "账号")
    private String username;

    @Schema(description = "姓名")
    private String realName;

    @Schema(description = "手机号")
    private String phone;

    @Schema(description = "邮箱")
    private String email;

    @Schema(description = "头像URL")
    private String avatar;

    @Schema(description = "账号状态")
    private Integer status;

    @Schema(description = "创建时间")
    private LocalDateTime createTime;
}
