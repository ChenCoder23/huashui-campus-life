package com.huashui.notification.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.huashui.notification.domain.pojo.NoticeReadRecord;
import com.huashui.notification.mapper.NoticeReadRecordMapper;
import com.huashui.notification.service.NoticeReadRecordService;
import org.springframework.stereotype.Service;

/**
 * 公告已读记录 Service 实现
 */
@Service
public class NoticeReadRecordServiceImpl extends ServiceImpl<NoticeReadRecordMapper, NoticeReadRecord> implements NoticeReadRecordService {

}