package com.huashui.notification.Enums;

import lombok.Getter;

/**
 * 消息状态
 */
@Getter
public enum MessageStatus {

    /**
     * 未读
     */
    UNREAD("UNREAD", "未读"),

    /**
     * 已读
     */
    READ("READ", "已读");

    private final String code;
    private final String desc;

    MessageStatus(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }
}