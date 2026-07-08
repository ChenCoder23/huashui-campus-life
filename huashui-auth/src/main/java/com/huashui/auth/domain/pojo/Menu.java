package com.huashui.auth.domain.pojo;

import com.baomidou.mybatisplus.annotation.*;
import com.huashui.auth.menu.HiddenEnum;
import com.huashui.auth.menu.HomeEnum;
import com.huashui.auth.menu.MenuType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 菜单实体
 *
 * @author 
 */
@Data
@TableName("sys_menu")
@Schema(description = "系统菜单实体")
public class Menu {

    @TableId(type = IdType.AUTO)
    @Schema(description = "主键ID")
    private Long id;


    @Schema(description = "父菜单ID（0=顶级）")
    private Long parentId;


    @Schema(description = "菜单名称")
    private String menuName;


    @Schema(description = "菜单类型：DIRECTORY-目录, MENU-菜单, BUTTON-按钮")
    private MenuType menuType;


    @Schema(description = "前端路由路径")
    private String path;


    @Schema(description = "前端组件路径")
    private String component;


    @Schema(description = "菜单图标")
    private String icon;


    @Schema(description = "权限标识（按钮级，如 repair:submit）")
    private String permission;


    @Schema(description = "排序序号")
    private Integer sortOrder;


    @Schema(description = "是否首页：0-否，1-是")
    private HomeEnum isHome;


    @Schema(description = "是否隐藏：0-否，1-是")
    private HiddenEnum hidden;


    @Schema(description = "状态：0-禁用，1-启用")
    private Integer status;


    @Schema(description = "创建时间")
    private LocalDateTime createTime;


    @Schema(description = "更新时间")
    private LocalDateTime updateTime;


    @TableLogic
    @Schema(description = "逻辑删除：0-未删除，1-已删除")
    private Integer isDeleted;


    /**
     * 非数据库字段
     * 用于菜单树返回
     */
    @TableField(exist = false)
    @Schema(description = "子菜单列表")
    private List<Menu> children;

}