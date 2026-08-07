package com.huashui.auth.domain.vo;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;


import java.time.LocalDateTime;


@Data
@Schema(description = "角色信息VO")
public class RoleVO {


    @Schema(description = "角色ID")
    private Long id;


    @Schema(description = "角色名称")
    private String roleName;


    @Schema(description = "角色编码")
    private String roleCode;


    @Schema(description = "角色描述")
    private String description;


    @Schema(description = "排序序号")
    private Integer sortOrder;


    @Schema(description = "状态 0-禁用 1-启用")
    private Integer status;


    @Schema(description = "创建时间")
    private LocalDateTime createTime;


    @Schema(description = "更新时间")
    private LocalDateTime updateTime;

}