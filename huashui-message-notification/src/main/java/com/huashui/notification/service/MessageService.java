package com.huashui.notification.service;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.huashui.notification.domain.pojo.Message;
public interface MessageService extends IService<Message> {
    Page<Message> page(Integer page, Integer size, String type);
    void markRead(Long id);
}