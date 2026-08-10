package com.huashui.evaluation.Enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 问卷状态
 *
 * WAITING 未开始
 * RUNNING 进行中
 * FINISHED 已结束
 * CANCELLED 已取消
 */
@Getter
@AllArgsConstructor
public enum QuestionStatus {

    /**
     * 未开始
     */
    WAITING("WAITING", "未开始"),

    /**
     * 进行中
     */
    RUNNING("RUNNING", "进行中"),

    /**
     * 已结束
     */
    FINISHED("FINISHED", "已结束"),

    /**
     * 已取消
     */
    CANCELLED("CANCELLED", "已取消");


    private final String value;

    private final String desc;
}