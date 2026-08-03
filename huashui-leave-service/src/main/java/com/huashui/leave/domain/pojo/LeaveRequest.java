package com.huashui.leave.domain.pojo;

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
@TableName("leave_request")
@Schema(description = "请假申请表")
public class LeaveRequest {

    @TableId(type = IdType.AUTO)
    private Long id;

    @Schema(description = "申请人ID")
    private Long applicantId;

    @Schema(description = "申请人姓名（冗余）")
    private String applicantName;

    @Schema(description = "申请人类型")
    private String applicantType;

    @Schema(description = "请假类型")
    private String leaveType;

    @Schema(description = "所在校区ID")
    private Long campusId;

    @Schema(description = "请假开始时间")
    private LocalDateTime startTime;

    @Schema(description = "请假结束时间")
    private LocalDateTime endTime;

    @Schema(description = "请假原因")
    private String reason;

    @Schema(description = "证明材料图片URL")
    private String proofImages;

    @Schema(description = "审批状态")
    private String status;

    @Schema(description = "审批人ID")
    private Long approverId;

    @Schema(description = "审批时间")
    private LocalDateTime approveTime;

    @Schema(description = "审批意见")
    private String approveOpinion;

    @Schema(description = "拒绝原因")
    private String rejectReason;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
}