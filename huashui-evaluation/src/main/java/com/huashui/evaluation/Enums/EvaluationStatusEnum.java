package com.huashui.evaluation.Enums;


import lombok.AllArgsConstructor;
import lombok.Getter;


@Getter
@AllArgsConstructor
public enum EvaluationStatusEnum {


    /**
     * 待评价
     */
    WAITING(
            "WAITING",
            "待评价"
    ),


    /**
     * 已完成
     */
    FINISHED(
            "FINISHED",
            "已完成"
    ),


    /**
     * 已过期
     */
    EXPIRED(
            "EXPIRED",
            "已过期"
    );



    private final String code;


    private final String desc;



    public static EvaluationStatusEnum of(String code){

        for(EvaluationStatusEnum value : values()){

            if(value.code.equals(code)){
                return value;
            }

        }

        throw new IllegalArgumentException(
                "未知评价状态:" + code
        );
    }

}