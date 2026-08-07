package com.huashui.attendance.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 考勤状态枚举
 *
 * NORMAL - 正常
 * LATE   - 迟到
 * ABSENT - 缺勤
 * LEAVE  - 请假
 * MAKEUP - 补签
 */
@Getter
@AllArgsConstructor
public enum AttendanceStatus {

    /**
     * 正常出勤
     */
    NORMAL("正常"),

    /**
     * 迟到
     */
    LATE("迟到"),

    /**
     * 缺勤
     */
    ABSENT("缺勤"),

    /**
     * 请假
     */
    LEAVE("请假"),

    /**
     * 补签
     */
    MAKEUP("补签");

    private final String desc;
}