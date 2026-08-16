package com.huashui.notification.domain.vo;

import lombok.Data;

/**
 * 消息列表VO
 */
@Data
public class MessageVO {

    /**
     * 消息ID
     */
    private Long id;

    /**
     * 消息标题
     */
    private String title;

    /**
     * 消息类型
     */
    private String type;
}