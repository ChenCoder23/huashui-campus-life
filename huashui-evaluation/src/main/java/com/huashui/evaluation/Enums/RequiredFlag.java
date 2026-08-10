package com.huashui.evaluation.Enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 是否必填
 *
 * 0 否
 * 1 是
 */
@Getter
@AllArgsConstructor
public enum RequiredFlag {


    /**
     * 非必填
     */
    NO(0, "否"),


    /**
     * 必填
     */
    YES(1, "是");


    private final Integer value;

    private final String desc;
}