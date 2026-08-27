package com.huashui.common.domain.mqMessage;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EvaluationEvent implements DelayedMessage {

    /**
     * 问卷ID
     */
    private Long questionnaireId;

    /**
     * 计划执行时间（问卷开始 / 结束时间），消费端与 DB 时间做等值校验
     */
    private LocalDateTime executeTime;

}