package com.huashui.notification.Enums;

import lombok.Getter;

/**
 * 消息优先级
 */
@Getter
public enum MessagePriority {

    /**
     * 普通
     */
    NORMAL("NORMAL", "普通"),

    /**
     * 重要
     */
    IMPORTANT("IMPORTANT", "重要"),

    /**
     * 紧急
     */
    URGENT("URGENT", "紧急");

    private final String code;
    private final String desc;

    MessagePriority(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }
}