package com.huashui.dormitory.domain.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
@Schema(description = "房间创建 DTO")
public class RoomCreateDTO {

    @NotNull(message = "楼栋ID不能为空")
    @Schema(description = "所属楼栋ID")
    private Long buildingId;

    @NotBlank(message = "房间号不能为空")
    @Schema(description = "房间号")
    private String roomNumber;

    @NotNull(message = "楼层不能为空")
    @Schema(description = "所在楼层")
    private Integer floorNumber;

    @NotBlank(message = "房型不能为空")
    @Schema(description = "房型")
    private String roomType;

    @NotNull(message = "床位数不能为空")
    @Schema(description = "总床位数")
    private Integer totalBeds;

    @Schema(description = "备注")
    private String remark;
}