package com.huashui.announcement.service.impl;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.huashui.announcement.domain.pojo.SystemNotice;
import com.huashui.announcement.mapper.SystemNoticeMapper;
import com.huashui.announcement.service.AnnouncementService;
import com.huashui.common.utils.UserContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service; import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;

@Slf4j @Service
public class AnnouncementServiceImpl extends ServiceImpl<SystemNoticeMapper, SystemNotice> implements AnnouncementService {
    @Override public Page<SystemNotice> page(Integer page, Integer size, String noticeType) {
        LambdaQueryWrapper<SystemNotice> qw = new LambdaQueryWrapper<>();
        if (noticeType != null) qw.eq(SystemNotice::getNoticeType, noticeType);
        qw.orderByDesc(SystemNotice::getIsTop).orderByDesc(SystemNotice::getCreateTime);
        return this.page(new Page<>(page, size), qw);
    }
    @Override @Transactional
    public void publish(Long id) {
        SystemNotice notice = getById(id);
        notice.setStatus("PUBLISHED"); notice.setPublishTime(LocalDateTime.now()); updateById(notice);
    }
    @Override @Transactional
    public void revoke(Long id) {
        SystemNotice notice = getById(id);
        notice.setStatus("REVOKED"); updateById(notice);
    }
}