package com.huashui.dormitory.Enum;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 卫浴类型枚举
 */
@Getter
@AllArgsConstructor
@Schema(description = "卫浴类型")
public enum BathType {

    @Schema(description = "独立卫浴")
    PRIVATE("PRIVATE", "独立卫浴"),

    @Schema(description = "公共隔间")
    PUBLIC_STALL("PUBLIC_STALL", "公共隔间"),

    @Schema(description = "干湿分离")
    DRY_WET_SEPARATED("DRY_WET_SEPARATED", "干湿分离");

    private final String code;

    private final String desc;
}