package com.huashui.dormitory.domain.pojo;

import com.baomidou.mybatisplus.annotation.*;
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
@TableName("dorm_building")
@Schema(description = "楼栋表")
public class DormBuilding {

    @TableId(type = IdType.AUTO)
    @Schema(description = "主键ID")
    private Long id;

    @Schema(description = "所属校区ID")
    private Long campusId;

    @Schema(description = "片区分组")
    private String area;

    @Schema(description = "楼栋名称")
    private String buildingName;

    @Schema(description = "楼栋编码")
    private String buildingCode;

    @Schema(description = "房型（FOUR-四人间，SIX-六人间）")
    private String roomType;

    @Schema(description = "总层数")
    private Integer totalFloors;

    @Schema(description = "住宿费（元/年）")
    private Integer accommodationFee;

    @Schema(description = "楼栋简介")
    private String description;

    @Schema(description = "是否标准化公寓")
    private Boolean isStandardized;

    @Schema(description = "排序序号")
    private Integer sortOrder;

    @Schema(description = "状态（0-停用，1-正常）")
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