package com.huashui.api.client.attendance;

import com.huashui.api.domain.dto.attendance.LeaveAttendanceDTO;
import com.huashui.common.response.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * 考勤服务内部 Feign 契约
 */
@FeignClient("huashui-attendance-service")
public interface AttendanceClient {

    /**
     * 保洁人员请假后，写入考勤记录（状态为请假）
     */
    @PostMapping("/attendance/inner/leave")
    Result<Void> addLeaveRecord(@RequestBody LeaveAttendanceDTO dto);
}
