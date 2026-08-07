package com.huashui.auth.Enum;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Schema(description = "性别")
public enum GenderType {


    FEMALE(0, "女"),


    MALE(1, "男");

    @Schema(description = "值")
    private final Integer value;

    @Schema(description = "描述")
    private final String desc;
}