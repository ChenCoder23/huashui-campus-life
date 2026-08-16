package com.huashui.attendance.api;

import com.huashui.api.domain.dto.attendance.LeaveAttendanceDTO;
import com.huashui.attendance.service.AttendanceRecordService;
import com.huashui.common.response.Result;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 考勤服务内部接口（供其他服务 Feign 调用）
 */
@RestController
@RequestMapping("/attendance/inner")
@RequiredArgsConstructor
public class AttendanceInnerApi {

    private final AttendanceRecordService attendanceService;

    /**
     * 保洁人员请假后写入考勤记录
     */
    @PostMapping("/leave")
    public Result<Void> addLeaveRecord(@RequestBody LeaveAttendanceDTO dto) {
        attendanceService.addLeaveRecord(dto);
        return Result.ok();
    }
}
