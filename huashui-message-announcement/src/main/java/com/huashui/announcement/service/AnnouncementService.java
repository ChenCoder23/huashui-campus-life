package com.huashui.announcement.service;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.huashui.announcement.domain.pojo.SystemNotice;
public interface AnnouncementService extends IService<SystemNotice> {
    Page<SystemNotice> page(Integer page, Integer size, String noticeType);
    void publish(Long id);
    void revoke(Long id);
}