package com.huashui.dormitory.Enum;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 房间状态
 */
@Getter
@AllArgsConstructor
@Schema(description = "房间状态")
public enum RoomStatus {

    @Schema(description = "正常，还可以入住")
    NORMAL("NORMAL", "正常"),

    @Schema(description = "住满")
    FULL("FULL", "住满"),

    @Schema(description = "空房")
    EMPTY("EMPTY", "空房"),

    @Schema(description = "封闭")
    LOCKED("LOCKED", "封闭");

    private final String code;

    private final String desc;
}