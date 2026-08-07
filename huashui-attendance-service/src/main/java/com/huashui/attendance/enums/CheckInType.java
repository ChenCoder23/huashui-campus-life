package com.huashui.attendance.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 打卡方式枚举
 *
 * GPS: GPS定位打卡
 * PHOTO: 拍照打卡
 */
@Getter
@AllArgsConstructor
public enum CheckInType {

    /**
     * GPS定位打卡
     */
    GPS("GPS", "GPS定位打卡"),

    /**
     * 拍照打卡
     */
    PHOTO("PHOTO", "拍照打卡");

    private final String code;
    private final String desc;
}