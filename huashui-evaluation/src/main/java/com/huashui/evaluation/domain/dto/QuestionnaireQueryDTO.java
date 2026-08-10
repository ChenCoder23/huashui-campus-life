package com.huashui.evaluation.domain.dto;

import com.huashui.common.domain.query.PageQuery;
import com.huashui.evaluation.Enums.QuestionStatus;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class QuestionnaireQueryDTO  extends PageQuery {



    /**
     * 问卷标题
     */
    private String title;

    /**
     * 状态
     */
    private QuestionStatus status;

    /**
     * 开始时间
     */
    private LocalDateTime startTime;

    /**
     * 结束时间
     */
    private LocalDateTime endTime;

}