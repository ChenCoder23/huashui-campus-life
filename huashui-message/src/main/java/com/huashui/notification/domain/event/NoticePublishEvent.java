package com.huashui.notification.domain.event;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 公告发布事件（MQ 消息体）
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NoticePublishEvent {

    /**
     * 公告ID
     */
    private Long noticeId;
}
