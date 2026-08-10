package com.huashui.evaluation.domain.pojo;


import com.baomidou.mybatisplus.annotation.*;
import com.huashui.evaluation.Enums.QuestionType;

import com.huashui.evaluation.Enums.RequiredFlag;
import lombok.Data;

import java.time.LocalDateTime;


@Data
@TableName("evaluation_question_item")
public class EvaluationQuestionItem {


    @TableId(type = IdType.AUTO)
    private Long id;


    /**
     * 所属问卷
     */
    private Long questionnaireId;


    /**
     * 问题内容
     */
    private String title;


    /**
     * 问题类型 SCORE/TEXT
     */
    private QuestionType type;


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
    private RequiredFlag requiredFlag;


    /**
     * 排序
     */
    private Integer sort;


    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

}