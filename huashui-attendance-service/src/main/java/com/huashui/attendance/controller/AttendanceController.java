package com.huashui.attendance.controller;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.huashui.attendance.domain.dto.AttendanceQueryDTO;
import com.huashui.attendance.domain.dto.CheckInDTO;
import com.huashui.attendance.domain.pojo.AttendanceRecord;
import com.huashui.attendance.domain.vo.AttendanceVO;
import com.huashui.attendance.service.AttendanceRecordService;
import com.huashui.common.response.PageResult;
import com.huashui.common.response.Result;
import com.huashui.common.utils.UserContext;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/attendance")
@RequiredArgsConstructor @Tag(name = "考勤管理")
public class AttendanceController {


    private final AttendanceRecordService attendanceService;

    /**
     * 查询今日考勤状态
     */
    @GetMapping("/today")
    @Operation(summary = "查看今日考勤状态")
    public Result<AttendanceVO> getTodayAttendance(){
        Long workerId = UserContext.getUserId();
        return Result.ok(attendanceService.getTodayAttendance(workerId));
    }



    /**
     * 查询自己的考勤记录
     */
    @GetMapping("/my")
    @Operation(summary = "查询我的考勤记录")
    public Result<List<AttendanceVO>> myAttendance(){
        return Result.ok(attendanceService.getMyAttendance());
    }



    /**
     * 工作人员签到
     *
     * GPS签到 / 拍照签到
     */
    @PostMapping("/check-in")
    public Result<Void> checkIn(@RequestBody CheckInDTO dto){
        Long workerId = UserContext.getUserId();
        dto.setWorkerId(workerId);
        attendanceService.checkIn(dto);
        return Result.ok();
    }


}