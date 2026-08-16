package com.huashui.notification.domain.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.huashui.notification.Enums.MessagePriority;
import com.huashui.notification.Enums.MessageStatus;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 系统消息
 */
@Data
@TableName("message")
public class Message {

    /**
     * 主键ID
     */
    @TableId(type = IdType.AUTO)
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
     * 接收用户ID
     */
    private Long receiverId;

    /**
     * 发送用户ID
     * 系统推送时为NULL
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
     * UNREAD-未读
     * READ-已读
     */
    private MessageStatus status;

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

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;
}