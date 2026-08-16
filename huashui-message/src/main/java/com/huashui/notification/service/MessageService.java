package com.huashui.notification.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.huashui.notification.domain.dto.MessageScrollQueryDTO;
import com.huashui.notification.domain.pojo.Message;
import com.huashui.notification.domain.vo.MessageDetailVO;
import com.huashui.notification.domain.vo.ScrollVO;
import com.huashui.notification.domain.vo.MessageVO;

/**
 * 消息 Service
 */
public interface MessageService extends IService<Message> {

    ScrollVO<MessageVO> scroll(MessageScrollQueryDTO dto);

    Long unreadCount();

    MessageDetailVO detail(Long id);

    void readAll();
}