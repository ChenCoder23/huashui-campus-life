package com.huashui.attendance.controller;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.huashui.attendance.domain.pojo.AttendanceRecord;
import com.huashui.attendance.service.AttendanceRecordService;
import com.huashui.common.response.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController @RequestMapping("/attendance") @RequiredArgsConstructor @Tag(name = "考勤管理")
public class AttendanceController {
    private final AttendanceRecordService attendanceService;
    @GetMapping @Operation(summary = "考勤列表")
    public Result<Page<AttendanceRecord>> list(@RequestParam(defaultValue = "1") Integer page, @RequestParam(defaultValue = "10") Integer size, @RequestParam(required = false) Long buildingId) {
        return Result.ok(attendanceService.page(page, size, buildingId));
    }
    @PostMapping("/check-in") @Operation(summary = "签到")
    public Result<Void> checkIn() { attendanceService.checkIn(); return Result.ok(); }
}