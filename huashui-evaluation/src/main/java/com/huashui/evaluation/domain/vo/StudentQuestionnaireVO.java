package com.huashui.evaluation.domain.vo;

import com.huashui.evaluation.Enums.QuestionStatus;
import lombok.Data;

import java.time.LocalDateTime;


/**
 * 学生待评价问卷VO
 */
@Data
public class StudentQuestionnaireVO {


    /**
     * 问卷ID
     */
    private Long id;


    /**
     * 问卷标题
     */
    private String title;


    /**
     * 问卷说明
     */
    private String description;


    /**
     * 开始时间
     */
    private LocalDateTime startTime;


    /**
     * 截止时间
     */
    private LocalDateTime endTime;


    /**
     * 问题数量
     */
    private Integer questionCount;


    /**
     * 问卷状态
     *
     * WAITING
     * RUNNING
     * FINISHED
     */
    private QuestionStatus status;




}