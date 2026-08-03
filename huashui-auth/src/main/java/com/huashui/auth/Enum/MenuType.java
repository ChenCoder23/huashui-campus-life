package com.huashui.auth.Enum;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Schema(description = "菜单类型")
public enum MenuType {


    DIRECTORY("DIRECTORY", "目录"),

    MENU("MENU", "菜单"),


    BUTTON("BUTTON", "按钮");

    @Schema(description = "编码")
    @EnumValue
    @JsonValue
    private final String code;

    @Schema(description = "描述")
    private final String desc;
}