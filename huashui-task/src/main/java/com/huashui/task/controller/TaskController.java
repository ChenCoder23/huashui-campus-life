package com.huashui.task.controller;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.huashui.common.response.Result;
import com.huashui.task.domain.pojo.CleanTask;
import com.huashui.task.domain.pojo.CleanWorkLog;
import com.huashui.task.service.CleanTaskService;
import com.huashui.task.service.CleanWorkLogService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController @RequestMapping("/task") @RequiredArgsConstructor @Tag(name = "工作任务管理")
public class TaskController {
    private final CleanTaskService taskService;
    private final CleanWorkLogService logService;

    @GetMapping @Operation(summary = "任务列表")
    public Result<Page<CleanTask>> list(@RequestParam(defaultValue = "1") Integer page, @RequestParam(defaultValue = "10") Integer size, @RequestParam(required = false) String status) {
        return Result.ok(taskService.page(page, size, status));
    }
    @PostMapping @Operation(summary = "宿管分配任务")
    public Result<Void> create(@RequestBody CleanTask task) { taskService.create(task); return Result.ok(); }
    @PutMapping("/{id}/complete") @Operation(summary = "完成任务")
    public Result<Void> complete(@PathVariable Long id, @RequestParam(required = false) String image) { taskService.complete(id, image); return Result.ok(); }
    @GetMapping("/logs") @Operation(summary = "工作记录")
    public Result<Page<CleanWorkLog>> logs(@RequestParam(defaultValue = "1") Integer page, @RequestParam(defaultValue = "10") Integer size) {
        return Result.ok(logService.page(page, size));
    }
}