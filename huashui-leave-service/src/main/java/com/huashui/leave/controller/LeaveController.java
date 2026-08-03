package com.huashui.leave.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.huashui.common.response.Result;
import com.huashui.leave.domain.dto.LeaveSubmitDTO;
import com.huashui.leave.domain.pojo.LeaveRequest;
import com.huashui.leave.service.LeaveRequestService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/leave")
@RequiredArgsConstructor
@Tag(name = "请假管理")
public class LeaveController {

    private final LeaveRequestService leaveService;

    @GetMapping
    @Operation(summary = "请假列表")
    public Result<Page<LeaveRequest>> list(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(required = false) String status) {
        return Result.ok(leaveService.page(page, size, status));
    }

    @GetMapping("/{id}")
    @Operation(summary = "请假详情")
    public Result<LeaveRequest> detail(@PathVariable Long id) {
        return Result.ok(leaveService.getById(id));
    }

    @PostMapping
    @Operation(summary = "提交请假申请")
    public Result<Void> submit(@Valid @RequestBody LeaveSubmitDTO dto) {
        leaveService.submit(dto);
        return Result.ok();
    }

    @PutMapping("/{id}/approve")
    @Operation(summary = "审批通过")
    public Result<Void> approve(
            @PathVariable Long id,
            @RequestParam(required = false, defaultValue = "") String opinion) {
        leaveService.approve(id, opinion);
        return Result.ok();
    }

    @PutMapping("/{id}/reject")
    @Operation(summary = "审批拒绝")
    public Result<Void> reject(
            @PathVariable Long id,
            @RequestParam(required = false, defaultValue = "") String reason) {
        leaveService.reject(id, reason);
        return Result.ok();
    }

    @PutMapping("/{id}/cancel")
    @Operation(summary = "撤销请假")
    public Result<Void> cancel(@PathVariable Long id) {
        leaveService.cancel(id);
        return Result.ok();
    }
}