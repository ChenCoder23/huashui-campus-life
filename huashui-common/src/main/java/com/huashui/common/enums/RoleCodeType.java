package com.huashui.common.enums;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "角色编码")
public enum RoleCodeType {


    STUDENT("STUDENT", "学生"),


    CLEANER("CLEANER", "保洁"),

    @Schema(description = "维修工")
    REPAIRER("REPAIRER", "维修工"),


    DORM_MANAGER("DORM_MANAGER", "宿管"),


    SUPER_ADMIN("SUPER_ADMIN", "超级管理员");

    @Schema(description = "编码")
    private final String code;

    @Schema(description = "描述")
    private final String desc;

    RoleCodeType(String code, String desc) {
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