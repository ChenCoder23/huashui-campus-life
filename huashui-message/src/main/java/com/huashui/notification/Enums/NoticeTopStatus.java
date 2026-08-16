package com.huashui.notification.Enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import lombok.Getter;

/**
 * 公告置顶状态
 */
@Getter
public enum NoticeTopStatus {

    NOT_TOP(0, "不置顶"),
    TOP(1, "置顶");

    @EnumValue
    private final Integer code;
    private final String desc;

    NoticeTopStatus(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }
}
