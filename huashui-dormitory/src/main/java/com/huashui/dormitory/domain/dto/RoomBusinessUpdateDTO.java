package com.huashui.dormitory.domain.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@Schema(description = "房间业务键编辑DTO")
public class RoomBusinessUpdateDTO extends RoomUpdateDTO {

    @NotNull(message = "原楼栋ID不能为空")
    @Schema(description = "原楼栋ID")
    private Long originalBuildingId;

    @NotBlank(message = "原房间号不能为空")
    @Schema(description = "原房间号")
    private String originalRoomNumber;
}
