package com.huashui.repair.domain.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
@Schema(description = "提交报修 DTO")
public class RepairSubmitDTO {

    @NotNull(message = "校区不能为空")
    private Long campusId;

    @NotNull(message = "楼栋不能为空")
    private Long buildingId;

    @NotNull(message = "房间不能为空")
    private Long roomId;

    @NotBlank(message = "报修类型不能为空")
    private String repairType;

    @NotBlank(message = "问题描述不能为空")
    private String description;

    @Schema(description = "故障图片URL数组")
    private String images;

    @NotBlank(message = "联系电话不能为空")
    private String contactPhone;

    @Schema(description = "预约维修时间段")
    private String appointmentTime;
}