package com.huashui.dormitory.Enum;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 阳台类型枚举
 */
@Getter
@AllArgsConstructor
@Schema(description = "阳台类型")
public enum BalconyType {

    @Schema(description = "标准阳台")
    STANDARD("STANDARD", "标准"),

    @Schema(description = "超大阳台")
    LARGE("LARGE", "超大");

    private final String code;

    private final String desc;
}