package com.huashui.evaluation.domain.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class CreateQuestionnaireDTO {


    /**
     * 问卷标题
     */
    @NotBlank(message = "问卷标题不能为空")
    private String title;


    /**
     * 问卷说明
     */
    private String description;


    /**
     * 开始时间
     */
    @NotNull(message = "开始时间不能为空")
    private LocalDateTime startTime;


    /**
     * 结束时间
     */
    @NotNull(message = "结束时间不能为空")
    private LocalDateTime endTime;



    /**
     * 评价范围类型
     *
     * STUDENT
     * BUILDING
     * CAMPUS
     * ALL
     */
    @NotBlank
    private String targetType;



    /**
     * 范围
     *
     *
     *
     *
     */
    private List<Long> targetScope;



    /**
     * 问题列表
     */
    @NotEmpty(message = "评价问题不能为空")
    private List<QuestionItemDTO> items;


}