package com.huashui.auth.domain.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * @author
 */
@Data
@Schema(description = "菜单新增/修改DTO")
public class MenuDTO {
    @Schema(description = "菜单ID，新增时为空，修改时必填")
    private Long id;

    @NotNull(message = "父菜单不能为空")
    @Schema(description = "父菜单ID（0=顶级菜单）")
    private Long parentId;

    @NotBlank(message = "菜单名称不能为空")
    @Size(max = 64, message = "菜单名称不能超过64个字符")
    @Schema(description = "菜单名称")
    private String menuName;

    @NotBlank(message = "菜单类型不能为空")
    @Schema(description = "菜单类型：DIRECTORY、MENU、BUTTON")
    private String menuType;

    @Size(max = 128, message = "路由路径不能超过128个字符")
    @Schema(description = "前端路由路径")
    private String path;

    @Size(max = 128, message = "组件路径不能超过128个字符")
    @Schema(description = "前端组件路径")
    private String component;

    @Size(max = 64, message = "图标不能超过64个字符")
    @Schema(description = "菜单图标")
    private String icon;

    @Size(max = 64, message = "权限标识不能超过64个字符")
    @Schema(description = "权限标识（按钮权限）")
    private String permission;

    @NotNull(message = "排序不能为空")
    @Min(value = 0, message = "排序不能小于0")
    @Schema(description = "排序")
    private Integer sortOrder;

    @NotNull(message = "是否首页不能为空")
    @Schema(description = "是否首页：0-否，1-是")
    private Integer isHome;

    @NotNull(message = "是否隐藏不能为空")
    @Schema(description = "是否隐藏：0-否，1-是")
    private Integer hidden;

    @NotNull(message = "状态不能为空")
    @Schema(description = "状态：0-禁用，1-启用")
    private Integer status;
}
