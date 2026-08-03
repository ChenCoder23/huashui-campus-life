package com.huashui.common.enums;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "登录方式")
public enum LoginType {

    PHONE("PHONE", "手机号登录"),


    ACCOUNT("ACCOUNT", "账号登录"),


    EMAIL("EMAIL", "邮箱登录");

    @Schema(description = "编码")
    private final String code;

    @Schema(description = "描述")
    private final String desc;

    LoginType(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public String getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }
}