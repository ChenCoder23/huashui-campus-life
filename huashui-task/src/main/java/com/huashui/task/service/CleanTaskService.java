package com.huashui.task.service;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.huashui.task.domain.pojo.CleanTask;
public interface CleanTaskService extends IService<CleanTask> {
    Page<CleanTask> page(Integer page, Integer size, String status);
    void create(CleanTask task);
    void complete(Long id, String completeImage);
}