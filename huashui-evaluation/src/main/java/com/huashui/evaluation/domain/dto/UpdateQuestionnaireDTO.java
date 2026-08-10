package com.huashui.evaluation.domain.dto;

import com.huashui.evaluation.Enums.ScopeType;

import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;


/**
 * 修改评价问卷DTO
 */
@Data
public class UpdateQuestionnaireDTO {


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
     * 结束时间
     */
    private LocalDateTime endTime;


    /**
     * 评价目标类型
     *
     * STUDENT
     * BUILDING
     * CAMPUS
     * ALL
     */
    private ScopeType targetType;


    /**
     * 评价范围
     *
     * JSON数组
     *
     * 例如:
     * [1,2,3]
     *
     * STUDENT:
     * 学生ID列表
     *
     * BUILDING:
     * 楼栋ID列表
     *
     * CAMPUS:
     * 校区ID列表
     *
     * ALL:
     * []
     */
    private List<Long> targetScope;


    /**
     * 问题列表
     */
    private List<QuestionItemDTO> questions;

}