package com.huashui.evaluation.domain.pojo;


import com.baomidou.mybatisplus.annotation.*;
import com.huashui.evaluation.Enums.EvaluationStatusEnum;
import com.huashui.evaluation.Enums.EvaluationTargetTypeEnum;
import lombok.Data;

import java.time.LocalDateTime;


@Data
@TableName("evaluation_record")
public class EvaluationRecord {


    @TableId(type = IdType.AUTO)
    private Long id;


    /**
     * 评价模板
     */
    private Long templateId;


    /**
     * 评价人学生ID
     */
    private Long evaluatorId;


    /**
     * 评价对象类型
     */
    private EvaluationTargetTypeEnum targetType;


    /**
     * 业务ID
     */
    private Long targetId;


    /**
     * 被评价人员
     */
    private Long targetUserId;


    private Long campusId;


    private Long buildingId;


    private Long roomId;


    /**
     * WAITING
     * FINISHED
     * EXPIRED
     */
    private EvaluationStatusEnum status;


    /**
     * 文字建议
     */
    private String suggestion;


    private LocalDateTime submitTime;


    private LocalDateTime createTime;

}