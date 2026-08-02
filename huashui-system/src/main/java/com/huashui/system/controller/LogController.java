package com.huashui.system.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.huashui.common.response.Result;
import com.huashui.system.domain.pojo.OperationLog;
import com.huashui.system.domain.pojo.SysExceptionLog;
import com.huashui.system.domain.pojo.SysLoginLog;
import com.huashui.system.service.LogService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/system/log")
@RequiredArgsConstructor
@Tag(name = "日志管理")
public class LogController {

    private final LogService logService;

    @GetMapping("/login")
    @Operation(summary = "登录日志")
    public Result<Page<SysLoginLog>> loginLog(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(required = false) String keyword) {
        return Result.ok(logService.loginLogPage(page, size, keyword));
    }

    @GetMapping("/operation")
    @Operation(summary = "操作日志")
    public Result<Page<OperationLog>> operationLog(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(required = false) String module) {
        return Result.ok(logService.operationLogPage(page, size, module));
    }

    @GetMapping("/exception")
    @Operation(summary = "异常日志")
    public Result<Page<SysExceptionLog>> exceptionLog(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(required = false) Integer status) {
        return Result.ok(logService.exceptionLogPage(page, size, status));
    }

    @GetMapping("/exception/{id}")
    @Operation(summary = "异常日志详情")
    public Result<SysExceptionLog> exceptionDetail(@PathVariable Long id) {
        return Result.ok(logService.getExceptionDetail(id));
    }
}