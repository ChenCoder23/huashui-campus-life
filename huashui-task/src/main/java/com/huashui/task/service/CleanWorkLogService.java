package com.huashui.task.service;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.huashui.task.domain.pojo.CleanWorkLog;
public interface CleanWorkLogService extends IService<CleanWorkLog> {
    Page<CleanWorkLog> page(Integer page, Integer size);
}