package com.huashui.evaluation.domain.vo;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class QuestionnaireVO {

    private Long id;

    /**
     * 问卷标题
     */
    private String title;


    /**
     * 状态
     */
    private String status;


    /**
     * 评价目标类型
     */
    private String targetType;


    /**
     * 开始时间
     */
    private LocalDateTime startTime;


    /**
     * 结束时间
     */
    private LocalDateTime endTime;


    /**
     * 总参与人数
     */
    private Integer totalCount;


    /**
     * 已提交人数
     */
    private Integer submitCount;


    /**
     * 创建时间
     */
    private LocalDateTime createTime;


}