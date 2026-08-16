package com.huashui.notification.domain.dto;

import com.huashui.notification.Enums.MessageStatus;
import lombok.Data;

/**
 * 消息滚动分页查询 DTO
 */
@Data
public class MessageScrollQueryDTO {

    private Long cursor;

    private Integer limit = 20;

    private String type;

    private MessageStatus status;
}
