package com.huashui.evaluation.domain.pojo;


import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;


import java.time.LocalDateTime;



@Data
@TableName("evaluation_answer")
public class EvaluationAnswer {


    @TableId(type = IdType.AUTO)
    private Long id;


    /**
     * 评价记录ID
     */
    private Long responseId;


    /**
     * 问题ID
     */
    private Long questionId;


    /**
     * 评分
     */
    private Integer score;


    /**
     * 文字回答
     */
    private String content;


    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

}