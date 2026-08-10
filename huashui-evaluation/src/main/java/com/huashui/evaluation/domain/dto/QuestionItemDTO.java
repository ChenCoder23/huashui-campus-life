package com.huashui.evaluation.domain.dto;

import com.huashui.evaluation.Enums.QuestionType;
import com.huashui.evaluation.Enums.RequiredFlag;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class QuestionItemDTO {


    /**
     * 问题标题
     */
    @NotBlank
    private String title;



    /**
     * SCORE
     * TEXT
     */
    @NotBlank
    private QuestionType type;



    /**
     * 最低分
     */
    private Integer minScore = 0;



    /**
     * 最高分
     */
    private Integer maxScore = 5;



    /**
     * 是否必填
     */
    private RequiredFlag requiredFlag ;



    /**
     * 排序
     */
    private Integer sort = 0;

}