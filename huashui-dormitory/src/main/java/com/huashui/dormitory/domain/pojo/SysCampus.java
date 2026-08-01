package com.huashui.dormitory.domain.pojo;

import com.baomidou.mybatisplus.annotation.*;
import com.huashui.common.domain.pojo.BaseEntity;
import com.huashui.common.enums.Status;
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
@TableName("sys_campus")
@Schema(description = "校区表")
public class SysCampus extends BaseEntity {

    @TableId(type = IdType.AUTO)
    @Schema(description = "主键ID")
    private Long id;

    @Schema(description = "校区名称")
    private String campusName;

    @Schema(description = "校区编码")
    private String campusCode;

    @Schema(description = "校区地址")
    private String address;

    @Schema(description = "排序序号")
    private Integer sortOrder;

    @Schema(description = "状态（0-停用，1-启用）")
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