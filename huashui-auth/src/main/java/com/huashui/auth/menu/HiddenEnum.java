package com.huashui.auth.menu;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Schema(description = "是否隐藏")
public enum HiddenEnum {


    NO(0, "否"),


    YES(1, "是");

    @Schema(description = "值")
    private final Integer value;

    @Schema(description = "描述")
    private final String desc;
}