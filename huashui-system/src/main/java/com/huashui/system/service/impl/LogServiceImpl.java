package com.huashui.system.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.huashui.common.exception.BusinessException;
import com.huashui.system.domain.pojo.OperationLog;
import com.huashui.system.domain.pojo.SysExceptionLog;
import com.huashui.system.domain.pojo.SysLoginLog;
import com.huashui.system.mapper.OperationLogMapper;
import com.huashui.system.mapper.SysExceptionLogMapper;
import com.huashui.system.mapper.SysLoginLogMapper;
import com.huashui.system.service.LogService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class LogServiceImpl implements LogService {

    private final SysLoginLogMapper loginLogMapper;
    private final OperationLogMapper operationLogMapper;
    private final SysExceptionLogMapper exceptionLogMapper;

    @Override
    public Page<SysLoginLog> loginLogPage(Integer page, Integer size, String keyword) {
        LambdaQueryWrapper<SysLoginLog> qw = new LambdaQueryWrapper<>();
        if (StrUtil.isNotBlank(keyword)) qw.like(SysLoginLog::getUsername, keyword);
        qw.orderByDesc(SysLoginLog::getLoginTime);
        return loginLogMapper.selectPage(new Page<>(page, size), qw);
    }

    @Override
    public Page<OperationLog> operationLogPage(Integer page, Integer size, String module) {
        LambdaQueryWrapper<OperationLog> qw = new LambdaQueryWrapper<>();
        if (StrUtil.isNotBlank(module)) qw.eq(OperationLog::getOperationModule, module);
        qw.orderByDesc(OperationLog::getCreateTime);
        return operationLogMapper.selectPage(new Page<>(page, size), qw);
    }

    @Override
    public Page<SysExceptionLog> exceptionLogPage(Integer page, Integer size, Integer status) {
        LambdaQueryWrapper<SysExceptionLog> qw = new LambdaQueryWrapper<>();
        if (status != null) qw.eq(SysExceptionLog::getStatus, status);
        qw.orderByDesc(SysExceptionLog::getCreateTime);
        return exceptionLogMapper.selectPage(new Page<>(page, size), qw);
    }

    @Override
    public SysExceptionLog getExceptionDetail(Long id) {
        SysExceptionLog log = exceptionLogMapper.selectById(id);
        if (log == null) throw new BusinessException("日志不存在");
        return log;
    }
}