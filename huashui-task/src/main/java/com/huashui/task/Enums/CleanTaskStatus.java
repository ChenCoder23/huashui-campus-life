package com.huashui.task.Enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum CleanTaskStatus {

    /**
     * 待执行
     */
    TODO("TODO", "待执行"),

    /**
     * 执行中
     */
    DOING("DOING", "执行中"),

    /**
     * 已完成
     */
    COMPLETED("COMPLETED", "已完成"),

    /**
     * 已取消
     */
    CANCELLED("CANCELLED", "已取消"),

    /**
     * 超时完成
     */
    TIMEOUT_COMPLETED("TIMEOUT_COMPLETED", "超时完成");


    private final String code;

    private final String desc;


    /**
     * 根据code获取枚举
     */
    public static CleanTaskStatus of(String code) {

        for (CleanTaskStatus status : values()) {

            if (status.code.equals(code)) {
                return status;
            }

        }

        throw new IllegalArgumentException("未知任务状态: " + code);
    }
}