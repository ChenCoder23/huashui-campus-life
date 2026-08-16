package com.huashui.notification.domain.vo;

import com.huashui.notification.Enums.MessagePriority;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 消息详情VO
 */
@Data
public class MessageDetailVO {

    /**
     * 消息ID
     */
    private Long id;

    /**
     * 消息类型
     */
    private String type;

    /**
     * 消息标题
     */
    private String title;

    /**
     * 消息正文
     */
    private String content;

    /**
     * 发送用户ID
     */
    private Long senderId;

    /**
     * 关联业务类型
     */
    private String businessType;

    /**
     * 关联业务ID
     */
    private Long businessId;

    /**
     * 消息状态
     */
    private String status;

    /**
     * 阅读时间
     */
    private LocalDateTime readTime;

    /**
     * 优先级
     */
    private MessagePriority priority;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;
}