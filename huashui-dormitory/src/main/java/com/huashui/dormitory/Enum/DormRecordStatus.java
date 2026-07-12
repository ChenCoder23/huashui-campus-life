package com.huashui.dormitory.Enum;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 住宿状态
 */
@Getter
@AllArgsConstructor
@Schema(description = "住宿状态")
public enum DormRecordStatus {

    @Schema(description = "已退宿")
    LEFT(0, "已退宿"),

    @Schema(description = "在住")
    LIVING(1, "在住");

    private final Integer code;

    private final String desc;
}