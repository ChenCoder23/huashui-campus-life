package com.huashui.dormitory.Enum;

import com.baomidou.mybatisplus.annotation.EnumValue;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 床位状态枚举
 */
@Getter
@AllArgsConstructor
@Schema(description = "床位状态")
public enum BedStatus {

    @Schema(description = "空闲")
    FREE(0, "空闲"),

    @Schema(description = "已入住")
    OCCUPIED(1, "已入住"),

    @Schema(description = "预留")
    RESERVED(2, "预留");


    @EnumValue
    private final Integer code;


    private final String desc;
}