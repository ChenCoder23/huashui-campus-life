package com.huashui.evaluation.domain.pojo;


import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.time.LocalDateTime;


@Data
@TableName("evaluation_score")
public class EvaluationScore {


    @TableId(type = IdType.AUTO)
    private Long id;


    /**
     * 评价记录ID
     */
    private Long recordId;


    /**
     * 评价维度ID
     */
    private Long dimensionId;


    /**
     * 维度快照
     */
    private String dimensionName;


    /**
     * 1-5分
     */
    private Integer score;


    private LocalDateTime createTime;

}