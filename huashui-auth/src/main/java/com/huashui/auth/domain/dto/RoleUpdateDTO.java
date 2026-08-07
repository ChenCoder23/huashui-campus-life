package com.huashui.auth.domain.dto;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;


@Data
@Schema(description = "角色修改DTO")
public class RoleUpdateDTO {


    @Schema(description = "角色名称")
    private String roleName;


    @Schema(description = "角色描述")
    private String description;


    @Schema(description = "排序序号")
    private Integer sortOrder;


    @Schema(description = "状态 0-禁用 1-启用")
    private Integer status;

}