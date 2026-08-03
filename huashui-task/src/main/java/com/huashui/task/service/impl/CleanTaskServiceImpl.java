package com.huashui.task.service.impl;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.huashui.common.utils.UserContext;
import com.huashui.task.domain.pojo.CleanTask;
import com.huashui.task.mapper.CleanTaskMapper;
import com.huashui.task.service.CleanTaskService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service; import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;

@Slf4j @Service
public class CleanTaskServiceImpl extends ServiceImpl<CleanTaskMapper, CleanTask> implements CleanTaskService {
    @Override
    public Page<CleanTask> page(Integer page, Integer size, String status) {
        LambdaQueryWrapper<CleanTask> qw = new LambdaQueryWrapper<>();
        String roles = UserContext.getRoles();
        if (roles != null && (roles.contains("CLEANER") || roles.contains("REPAIRER"))) qw.eq(CleanTask::getWorkerId, UserContext.getUserId());
        if (status != null) qw.eq(CleanTask::getStatus, status);
        qw.orderByDesc(CleanTask::getCreateTime);
        return this.page(new Page<>(page, size), qw);
    }
    @Override @Transactional
    public void create(CleanTask task) { task.setStatus("TODO"); save(task); }
    @Override @Transactional
    public void complete(Long id, String completeImage) {
        CleanTask task = getById(id); task.setStatus("COMPLETED");
        task.setCompletedTime(LocalDateTime.now()); task.setCompleteImage(completeImage); updateById(task);
    }
}