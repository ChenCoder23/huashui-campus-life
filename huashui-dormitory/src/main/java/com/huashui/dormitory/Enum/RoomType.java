package com.huashui.dormitory.Enum;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 房型枚举
 */
@Getter
@AllArgsConstructor
@Schema(description = "房间类型")
public enum RoomType {

    @Schema(description = "四人间")
    FOUR("FOUR", "四人间"),

    @Schema(description = "六人间")
    SIX("SIX", "六人间");

    private final String code;

    private final String desc;
}