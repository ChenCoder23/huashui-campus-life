package com.huashui.evaluation.domain.pojo;

import com.baomidou.mybatisplus.annotation.*;
import com.huashui.common.enums.Status;
import com.huashui.evaluation.Enums.EvaluationTargetTypeEnum;
import lombok.Data;

import java.time.LocalDateTime;


@Data
@TableName("evaluation_template")
public class EvaluationTemplate {


    @TableId(type = IdType.AUTO)
    private Long id;


    /**
     * 模板名称
     */
    private String templateName;


    /**
     * REPAIR维修
     * DORM宿舍
     * CLEAN保洁
     */
    private EvaluationTargetTypeEnum type;


    /**
     * 创建人
     */
    private Long creatorId;


    /**
     * ACTIVE启用
     * DISABLED禁用
     */
    private Status status;


    private LocalDateTime createTime;


    private LocalDateTime updateTime;

}