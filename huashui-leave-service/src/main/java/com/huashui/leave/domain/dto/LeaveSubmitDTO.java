package com.huashui.leave.domain.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Schema(description = "提交请假 DTO")
public class LeaveSubmitDTO {

    @NotBlank(message = "请假类型不能为空")
    private String leaveType;

    @NotNull(message = "开始时间不能为空")
    private LocalDateTime startTime;

    @NotNull(message = "结束时间不能为空")
    private LocalDateTime endTime;

    @NotBlank(message = "请假原因不能为空")
    private String reason;

    @Schema(description = "证明材料图片URL")
    private String proofImages;

    @NotNull(message = "校区不能为空")
    private Long campusId;
}