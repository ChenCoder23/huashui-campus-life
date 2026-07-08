package com.huashui.auth.menu;

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
    private final String code;

    @Schema(description = "描述")
    private final String desc;
}