package com.huashui.dormitory.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.huashui.common.response.Result;
import com.huashui.dormitory.domain.dto.RecordAdjustDTO;
import com.huashui.dormitory.domain.dto.RecordAssignDTO;
import com.huashui.dormitory.domain.pojo.DormStudentRecord;
import com.huashui.dormitory.service.DormStudentRecordService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/dormitory/record")
@RequiredArgsConstructor
@Tag(name = "住宿记录")
public class DormRecordController {

    private final DormStudentRecordService recordService;

    @GetMapping
    @Operation(summary = "住宿记录列表（分页）")
    public Result<Page<DormStudentRecord>> list(
            // todo 使用dto
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(required = false) Long buildingId,
            @RequestParam(required = false) Long studentId) {
        return Result.ok(recordService.page(page, size, buildingId, studentId));
    }

    @PostMapping("/assign")
    @Operation(summary = "分配床位给学生")
    public Result<Void> assign(@Valid @RequestBody RecordAssignDTO dto) {
        recordService.assign(dto);
        return Result.ok();
    }

    @PostMapping("/adjust")
    @Operation(summary = "调整学生房间/床位")
    public Result<Void> adjust(@Valid @RequestBody RecordAdjustDTO dto) {
        recordService.adjust(dto);
        return Result.ok();
    }

    @PostMapping("/checkout")
    @Operation(summary = "办理退宿")
    public Result<Void> checkout(@RequestParam Long studentId) {
        recordService.checkout(studentId);
        return Result.ok();
    }

    @GetMapping("/student/{studentId}")
    @Operation(summary = "查询学生当前住宿记录")
    public Result<DormStudentRecord> studentRecord(@PathVariable Long studentId) {
        return Result.ok(recordService.getByStudentId(studentId));
    }

    @PostMapping("/import")
    @Operation(summary = "批量导入新生入住")
    public Result<Void> importRecords(@RequestParam String fileUrl) {
        recordService.importRecords(fileUrl);
        return Result.ok();
    }

    @GetMapping("/export")
    @Operation(summary = "导出住宿名单")
    public Result<Void> exportRecords(@RequestParam(required = false) Long buildingId) {
        recordService.exportRecords(buildingId);
        return Result.ok();
    }
}
