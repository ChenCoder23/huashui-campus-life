package com.huashui.dormitory.Enum;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 床铺类型枚举
 */
@Getter
@AllArgsConstructor
@Schema(description = "床铺类型")
public enum BedType {

    @Schema(description = "上床下桌")
    BUNK_DESK("BUNK_DESK", "上床下桌"),

    @Schema(description = "上下铺混合")
    BUNK_MIXED("BUNK_MIXED", "上下铺混合");

    private final String code;

    private final String desc;
}