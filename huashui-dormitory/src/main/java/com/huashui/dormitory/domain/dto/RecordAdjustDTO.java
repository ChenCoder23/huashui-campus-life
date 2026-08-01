package com.huashui.dormitory.domain.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
@Schema(description = "调宿 DTO")
public class RecordAdjustDTO {

    @NotNull(message = "学生ID不能为空")
    @Schema(description = "学生用户ID")
    private Long studentId;

    @NotNull(message = "新房间ID不能为空")
    @Schema(description = "新房间ID")
    private Long newRoomId;

    @Schema(description = "新床位ID（不传则自动分配）")
    private Long newBedId;
}