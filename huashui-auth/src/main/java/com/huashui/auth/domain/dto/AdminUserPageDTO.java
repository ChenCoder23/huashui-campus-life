package com.huashui.auth.domain.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
@Schema(description = "管理员用户分页查询DTO")
public class AdminUserPageDTO {

    @Schema(description = "页码")
    private Long pageNum = 1L;

    @Schema(description = "每页数量")
    private Long pageSize = 10L;

    @NotBlank(message = "角色名称不能为空")
    @Schema(description = "角色名称")
    private String roleName;

    @Schema(description = "账号/姓名/手机号关键词")
    private String keyword;
}
