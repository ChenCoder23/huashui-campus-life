package com.huashui.evaluation.domain.pojo;


import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import com.huashui.common.enums.Status;

import com.huashui.evaluation.Enums.QuestionStatus;

import com.huashui.evaluation.Enums.ScopeType;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * 评价问卷表
 */
@Data
@TableName(value = "evaluation_questionnaire",autoResultMap = true)
public class EvaluationQuestionnaire {


    @TableId(type = IdType.AUTO)
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
     * 创建管理员ID
     */
    private Long creatorId;


    /**
     * 开始时间
     */
    private LocalDateTime startTime;


    /**
     * 截止时间
     */
    private LocalDateTime endTime;


    /**
     * 状态
     */
    private QuestionStatus status;


    /**
     * 目标类型
     */
    private ScopeType targetType;


    /**
     * 评价范围
     */
    @TableField(typeHandler = JacksonTypeHandler.class)
    private List<Long> targetScope;


    /**
     * 需要评价人数
     */
    private Integer totalCount;


    /**
     * 已提交人数
     */
    private Integer submitCount;


    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;


    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;



}