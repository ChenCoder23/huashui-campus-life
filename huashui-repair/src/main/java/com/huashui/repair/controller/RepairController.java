package com.huashui.repair.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.huashui.common.response.Result;
import com.huashui.repair.domain.dto.RepairSubmitDTO;
import com.huashui.repair.domain.pojo.RepairOrder;
import com.huashui.repair.service.RepairOrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/repair")
@RequiredArgsConstructor
@Tag(name = "报修工单管理")
public class RepairController {

    private final RepairOrderService repairService;

    @GetMapping
    @Operation(summary = "工单列表")
    public Result<Page<RepairOrder>> list(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) Long buildingId) {
        return Result.ok(repairService.page(page, size, status, buildingId));
    }

    @GetMapping("/{id}")
    @Operation(summary = "工单详情")
    public Result<RepairOrder> detail(@PathVariable Long id) {
        return Result.ok(repairService.getById(id));
    }

    @PostMapping
    @Operation(summary = "学生提交报修")
    public Result<Void> submit(@Valid @RequestBody RepairSubmitDTO dto) {
        repairService.submit(dto);
        return Result.ok();
    }

    @PutMapping("/{id}/assign")
    @Operation(summary = "宿管派单")
    public Result<Void> assign(@PathVariable Long id, @RequestParam Long repairerId) {
        repairService.assign(id, repairerId);
        return Result.ok();
    }

    @PutMapping("/{id}/start")
    @Operation(summary = "维修工接单开始维修")
    public Result<Void> startRepair(@PathVariable Long id) {
        repairService.startRepair(id);
        return Result.ok();
    }

    @PutMapping("/{id}/complete")
    @Operation(summary = "维修工完成维修")
    public Result<Void> complete(
            @PathVariable Long id,
            @RequestParam(required = false) String repairResult,
            @RequestParam(required = false) String repairImages) {
        repairService.complete(id, repairResult, repairImages);
        return Result.ok();
    }

    @PutMapping("/{id}/evaluate")
    @Operation(summary = "学生评价工单")
    public Result<Void> evaluate(@PathVariable Long id, @RequestParam Integer rating) {
        repairService.evaluate(id, rating);
        return Result.ok();
    }

    @PutMapping("/{id}/cancel")
    @Operation(summary = "取消工单")
    public Result<Void> cancel(@PathVariable Long id) {
        repairService.cancel(id);
        return Result.ok();
    }

    @GetMapping("/export")
    @Operation(summary = "导出工单")
    public Result<Void> exportData(
            @RequestParam(required = false) String status,
            @RequestParam(required = false) Long buildingId) {
        repairService.exportData(status, buildingId);
        return Result.ok();
    }
}
