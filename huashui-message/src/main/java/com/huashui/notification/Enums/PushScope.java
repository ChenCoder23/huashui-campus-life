package com.huashui.notification.Enums;

import lombok.Getter;

/**
 * 公告推送范围
 */
@Getter
public enum PushScope {

    /**
     * 全部用户
     */
    ALL("ALL", "全部"),

    /**
     * 指定校区
     */
    CAMPUS("CAMPUS", "指定校区"),

    /**
     * 指定角色
     */
    ROLE("ROLE", "指定角色"),

    /**
     * 指定楼栋
     */
    BUILDING("BUILDING", "指定楼栋");

    private final String code;
    private final String desc;

    PushScope(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }
}