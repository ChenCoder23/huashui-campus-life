package com.huashui.auth.domain.pojo;

import com.baomidou.mybatisplus.annotation.*;
import com.huashui.common.enums.RoleCodeType;
import com.huashui.common.enums.Status;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 角色表
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("sys_role")
@Schema(description = "角色表")
public class SysRole {

    @TableId(type = IdType.AUTO)
    @Schema(description = "主键ID")
    private Long id;


    @Schema(description = "角色名称")
    private String roleName;


    @Schema(description = "角色编码")
    private RoleCodeType roleCode;


    @Schema(description = "角色描述")
    private String description;


    @Schema(description = "排序序号")
    private Integer sortOrder;


    @Schema(description = "状态（0-禁用，1-启用）")
    private Status status;


    @TableField(fill = FieldFill.INSERT)
    @Schema(description = "创建时间")
    private LocalDateTime createTime;


    @TableField(fill = FieldFill.INSERT_UPDATE)
    @Schema(description = "更新时间")
    private LocalDateTime updateTime;


    @TableLogic
    @Schema(description = "逻辑删除（0-未删除，1-已删除）")
    private Integer isDeleted;

}