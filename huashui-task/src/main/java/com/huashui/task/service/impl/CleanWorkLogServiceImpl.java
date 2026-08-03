package com.huashui.task.service.impl;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.huashui.common.utils.UserContext;
import com.huashui.task.domain.pojo.CleanWorkLog;
import com.huashui.task.mapper.CleanWorkLogMapper;
import com.huashui.task.service.CleanWorkLogService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j @Service
public class CleanWorkLogServiceImpl extends ServiceImpl<CleanWorkLogMapper, CleanWorkLog> implements CleanWorkLogService {
    @Override
    public Page<CleanWorkLog> page(Integer page, Integer size) {
        return lambdaQuery().eq(CleanWorkLog::getWorkerId, UserContext.getUserId()).orderByDesc(CleanWorkLog::getWorkDate).page(new Page<>(page, size));
    }
}