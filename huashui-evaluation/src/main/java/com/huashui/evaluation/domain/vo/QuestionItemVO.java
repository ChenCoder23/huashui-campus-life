package com.huashui.evaluation.domain.vo;

import lombok.Data;

@Data
public class QuestionItemVO {


    private Long id;


    /**
     * 问题内容
     */
    private String title;


    /**
     * 类型
     */
    private String type;


    /**
     * 最低分
     */
    private Integer minScore;


    /**
     * 最高分
     */
    private Integer maxScore;


    /**
     * 是否必填
     */
    private Integer requiredFlag;


    /**
     * 排序
     */
    private Integer sort;

}