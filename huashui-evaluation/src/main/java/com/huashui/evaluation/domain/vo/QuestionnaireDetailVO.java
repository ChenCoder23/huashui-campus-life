package com.huashui.evaluation.domain.vo;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class QuestionnaireDetailVO {


    /**
     * 问卷ID
     */
    private Long id;


    /**
     * 标题
     */
    private String title;


    /**
     * 描述
     */
    private String description;


    /**
     * 创建人
     */
    private Long creatorId;


    /**
     * 开始时间
     */
    private LocalDateTime startTime;


    /**
     * 结束时间
     */
    private LocalDateTime endTime;


    /**
     * 状态
     */
    private String status;


    /**
     * 目标类型
     */
    private String targetType;


    // todo 不展示id而是具体的字段name
    /**
     * 评价范围
     */
    private List<Long> targetScope;


    /**
     * 总人数
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


    /**
     * 问题列表
     */
    private List<QuestionItemVO> questions;

}