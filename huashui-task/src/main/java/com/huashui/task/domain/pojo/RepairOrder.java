package com.huashui.task.domain.pojo;

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
@TableName("repair_order")
@Schema(description = "报修工单表")
public class RepairOrder {

    @TableId(type = IdType.AUTO)
    private Long id;

    @Schema(description = "工单编号")
    private String orderNo;

    @Schema(description = "报修学生ID")
    private Long studentId;

    @Schema(description = "学生姓名（冗余）")
    private String studentName;

    @Schema(description = "校区ID")
    private Long campusId;

    @Schema(description = "楼栋ID")
    private Long buildingId;

    @Schema(description = "房间ID")
    private Long roomId;

    @Schema(description = "报修类型")
    private String repairType;

    @Schema(description = "问题描述")
    private String description;

    @Schema(description = "故障图片URL数组")
    private String images;

    @Schema(description = "联系电话")
    private String contactPhone;

    @Schema(description = "预约维修时间段")
    private String appointmentTime;

    @Schema(description = "工单状态")
    private String status;

    @Schema(description = "维修人员姓名（冗余）")
    private String repairerName;

    @Schema(description = "维修人员ID")
    private Long repairerId;

    @Schema(description = "派单人ID")
    private Long assignerId;

    @Schema(description = "派单时间")
    private LocalDateTime assignedTime;

    @Schema(description = "实际维修时间")
    private LocalDateTime repairTime;

    @Schema(description = "维修处理结果")
    private String repairResult;

    @Schema(description = "维修后照片URL数组")
    private String repairImages;

    @Schema(description = "学生评分（1-5）")
    private Integer rating;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
}