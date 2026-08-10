package com.huashui.evaluation.Enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 评价状态枚举
 *
 * DRAFT      未提交
 * SUBMITTED  已提交
 * EXPIRED    已过期
 */
@Getter
@AllArgsConstructor
public enum StudentEvaluationStatus {

    /**
     * 未提交
     */
    DRAFT("DRAFT", "未提交"),

    /**
     * 已提交
     */
    SUBMITTED("SUBMITTED", "已提交"),

    /**
     * 已过期
     */
    EXPIRED("EXPIRED", "已过期");

    private final String code;
    private final String desc;



}