package com.huashui.evaluation.domain.dto;

import lombok.Data;

import java.util.List;


/**
 * 提交评价DTO
 */
@Data
public class SubmitEvaluationDTO {


    /**
     * 答案列表
     */
    private List<AnswerDTO> answers;



    @Data
    public static class AnswerDTO {


        /**
         * 问题ID
         */
        private Long questionId;


        /**
         * 评分结果
         *
         * SCORE类型使用
         */
        private Integer score;


        /**
         * 文字回答
         *
         * TEXT类型使用
         */
        private String content;

    }

}