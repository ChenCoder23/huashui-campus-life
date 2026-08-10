package com.huashui.evaluation.domain.pojo;


import com.baomidou.mybatisplus.annotation.*;
import com.huashui.evaluation.Enums.StudentEvaluationStatus;
import lombok.Data;


import java.time.LocalDateTime;



@Data
@TableName("evaluation_response")
public class EvaluationResponse {


    @TableId(type = IdType.AUTO)
    private Long id;


    /**
     * 问卷ID
     */
    private Long questionnaireId;


    /**
     * 学生ID
     */
    private Long studentId;


    /**
     * 状态
     */
    private StudentEvaluationStatus status;


    /**
     * 提交时间
     */
    private LocalDateTime submitTime;


    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;


    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

}