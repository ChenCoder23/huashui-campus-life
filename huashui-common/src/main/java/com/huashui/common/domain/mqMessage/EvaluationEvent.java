package com.huashui.common.domain.mqMessage;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EvaluationEvent {

    /**
     * 问卷ID
     */
    private Long questionnaireId;

    /**
     * 消息版本号
     * 防止旧延迟消息执行
     */
    private Long version;

}