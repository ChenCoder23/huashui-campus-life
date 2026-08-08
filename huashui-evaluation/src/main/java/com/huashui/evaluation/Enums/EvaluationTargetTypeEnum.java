package com.huashui.evaluation.Enums;

import lombok.AllArgsConstructor;
import lombok.Getter;


@Getter
@AllArgsConstructor
public enum EvaluationTargetTypeEnum {


    /**
     * 维修订单评价
     */
    REPAIR_ORDER("REPAIR_ORDER", "维修订单"),


    /**
     * 宿舍服务评价
     */
    DORM_SERVICE("DORM_SERVICE", "宿舍服务"),


    /**
     * 保洁任务评价
     */
    CLEAN_TASK("CLEAN_TASK", "保洁任务");


    private final String code;

    private final String desc;



    public static EvaluationTargetTypeEnum of(String code){

        for(EvaluationTargetTypeEnum value : values()){

            if(value.code.equals(code)){
                return value;
            }

        }

        throw new IllegalArgumentException(
                "未知评价对象类型:" + code
        );
    }

}