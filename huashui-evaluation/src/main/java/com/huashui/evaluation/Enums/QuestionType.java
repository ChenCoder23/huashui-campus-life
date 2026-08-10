package com.huashui.evaluation.Enums;

import lombok.AllArgsConstructor;
import lombok.Getter;


/**
 * 问题类型
 *
 * SCORE 评分类型
 * TEXT 文字建议类型
 */
@Getter
@AllArgsConstructor
public enum QuestionType {


    /**
     * 评分类型
     */
    SCORE("SCORE", "评分类型"),


    /**
     * 文字建议类型
     */
    TEXT("TEXT", "文字建议类型");


    private final String value;

    private final String desc;
}