package com.huashui.dormitory.domain.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
@Schema(description = "批量创建房间 DTO")
public class RoomBatchCreateDTO {

    @NotNull(message = "楼栋ID不能为空")
    @Schema(description = "楼栋ID")
    private Long buildingId;

    @NotNull(message = "起始楼层不能为空")
    @Schema(description = "起始楼层")
    private Integer startFloor;

    @NotNull(message = "结束楼层不能为空")
    @Schema(description = "结束楼层")
    private Integer endFloor;

    @NotNull(message = "每层房间数不能为空")
    @Schema(description = "每层房间数")
    private Integer roomsPerFloor;

    //新增字段设置几人间
}