package com.huashui.notification.service.impl;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.huashui.common.utils.UserContext;
import com.huashui.notification.domain.pojo.Message;
import com.huashui.notification.mapper.MessageMapper;
import com.huashui.notification.service.MessageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service; import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;

@Slf4j @Service
public class MessageServiceImpl extends ServiceImpl<MessageMapper, Message> implements MessageService {
    @Override public Page<Message> page(Integer page, Integer size, String type) {
        return lambdaQuery().eq(Message::getReceiverId, UserContext.getUserId())
            .eq(type != null, Message::getType, type)
            .orderByDesc(Message::getCreateTime).page(new Page<>(page, size));
    }
    @Override @Transactional
    public void markRead(Long id) {
        lambdaUpdate().eq(Message::getId, id).set(Message::getStatus, "READ").set(Message::getReadTime, LocalDateTime.now()).update();
    }
}