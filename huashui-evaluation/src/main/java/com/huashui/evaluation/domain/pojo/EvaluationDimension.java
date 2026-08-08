package com.huashui.evaluation.domain.pojo;


import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.time.LocalDateTime;


@Data
@TableName("evaluation_dimension")
public class EvaluationDimension {


    @TableId(type = IdType.AUTO)
    private Long id;


    /**
     * 模板ID
     */
    private Long templateId;


    /**
     * 评价维度名称
     */
    private String name;


    /**
     * 排序
     */
    private Integer sort;


    private LocalDateTime createTime;

}