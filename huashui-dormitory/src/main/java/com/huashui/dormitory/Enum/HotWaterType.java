package com.huashui.dormitory.Enum;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 热水供应类型
 */
@Getter
@AllArgsConstructor
@Schema(description = "热水供应类型")
public enum HotWaterType {

    @Schema(description = "24小时供应")
    ALL_DAY("ALL_DAY", "24小时"),

    @Schema(description = "限时供应")
    LIMITED("LIMITED", "限时供应");

    private final String code;

    private final String desc;
}