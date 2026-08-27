package com.huashui.notification.Enums;

import lombok.Getter;

/**
 * 公告状态
 */
@Getter
public enum NoticeStatus {

    /**
     * 草稿
     */
    DRAFT("DRAFT", "草稿"),

    /**
     * 待发布（已设置定时发布时间，等待发布）
     */
    PENDING("PENDING", "待发布"),

    /**
     * 已发布
     */
    PUBLISHED("PUBLISHED", "已发布"),

    /**
     * 已过期
     */
    EXPIRED("EXPIRED", "已过期"),

    /**
     * 已撤回
     */
    REVOKED("REVOKED", "已撤回");

    private final String code;
    private final String desc;

    NoticeStatus(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }
}