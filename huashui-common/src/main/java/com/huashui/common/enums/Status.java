package com.huashui.common.enums;


import com.baomidou.mybatisplus.annotation.EnumValue;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "状态")
public enum Status {


    DISABLED(0, "禁用"),


    ENABLED(1, "启用");

    @Schema(description = "值")
    @EnumValue
    private final Integer value;

    @Schema(description = "描述")
    private final String desc;

    Status(Integer value, String desc) {
        this.value = value;
        this.desc = desc;
    }

    public Integer getValue() {
        return value;
    }

    public String getDesc() {
        return desc;
    }
}