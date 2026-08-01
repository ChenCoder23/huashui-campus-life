package com.huashui.dormitory.domain.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
@Schema(description = "床位分配 DTO")
public class RecordAssignDTO {

    @NotNull(message = "学生ID不能为空")
    @Schema(description = "学生用户ID")
    private Long studentId;

    @NotNull(message = "房间ID不能为空")
    @Schema(description = "房间ID")
    private Long roomId;

    @Schema(description = "床位ID（不传则自动分配空闲床位）")
    private Long bedId;
}