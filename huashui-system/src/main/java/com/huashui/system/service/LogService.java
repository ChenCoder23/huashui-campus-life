package com.huashui.system.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.huashui.system.domain.pojo.OperationLog;
import com.huashui.system.domain.pojo.SysExceptionLog;
import com.huashui.system.domain.pojo.SysLoginLog;

public interface LogService {
    Page<SysLoginLog> loginLogPage(Integer page, Integer size, String keyword);
    Page<OperationLog> operationLogPage(Integer page, Integer size, String module);
    Page<SysExceptionLog> exceptionLogPage(Integer page, Integer size, Integer status);
    SysExceptionLog getExceptionDetail(Long id);
}