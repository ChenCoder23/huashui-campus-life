package com.huashui.notification.domain.event;

import com.huashui.common.domain.mqMessage.DelayedMessage;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 公告发布事件（MQ 消息体）
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NoticePublishEvent implements DelayedMessage {

    /**
     * 公告ID
     */
    private Long noticeId;

    /**
     * 计划发布时间，消费端与 DB publish_time 做等值校验
     */
    private LocalDateTime executeTime;

}