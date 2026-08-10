package com.huashui.evaluation.Enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 评价目标类型
 *
 * STUDENT 指定学生
 * BUILDING 指定楼栋
 * CAMPUS 指定校区
 * ALL 全部学生
 */
@Getter
@AllArgsConstructor
public enum ScopeType {


    /**
     * 指定学生
     */
    STUDENT("STUDENT", "指定学生"),


    /**
     * 指定楼栋
     */
    BUILDING("BUILDING", "指定楼栋"),


    /**
     * 指定校区
     */
    CAMPUS("CAMPUS", "指定校区"),


    /**
     * 全部学生
     */
    ALL("ALL", "全部学生");


    private final String value;

    private final String desc;
}