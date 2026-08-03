package com.huashui.auth.Enum;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Schema(description = "是否首页")
public enum HomeEnum {


    NO(0, "否"),


    YES(1, "是");

    @Schema(description = "值")
    @EnumValue
    @JsonValue
    private final Integer value;

    @Schema(description = "描述")
    private final String desc;
}