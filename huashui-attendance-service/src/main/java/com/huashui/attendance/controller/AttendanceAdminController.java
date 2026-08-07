package com.huashui.attendance.controller;



import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.huashui.attendance.domain.dto.AttendanceQueryDTO;
import com.huashui.attendance.domain.dto.AttendanceStatusUpdateDTO;
import com.huashui.attendance.domain.vo.AttendanceStatisticsVO;
import com.huashui.attendance.domain.vo.AttendanceVO;
import com.huashui.attendance.service.AttendanceRecordService;
import com.huashui.common.response.PageResult;
import com.huashui.common.response.Result;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;



/**
 * 管理端考勤管理
 */
@RestController
@RequestMapping("/admin/attendance")
public class AttendanceAdminController {


    @Resource
    private AttendanceRecordService attendanceService;


    /**
     * 分页查询考勤
     */
    @GetMapping("/page")
    @Operation(summary="分页查询考勤记录")
    public PageResult<AttendanceVO> page(AttendanceQueryDTO dto){
        return attendanceService.pageQuery(dto);
    }


    /**
     * 查询员工考勤
     */
    @GetMapping("/worker/{workerId}")
    @Operation(summary="查询员工考勤")
    public  PageResult<AttendanceVO> workerAttendance(@PathVariable Long workerId, AttendanceQueryDTO dto){
        dto.setWorkerId(workerId);
        return attendanceService.pageQuery(dto);
    }





    /**
     * 管理员补签/修改考勤
     */
    @PutMapping("/update")
    @Operation(summary="修改考勤")
    public Result<Void> update(@RequestBody AttendanceStatusUpdateDTO dto){
        attendanceService.updateAttendance(dto);
        return Result.ok();
    }





    /**
     * 考勤统计
     */
    @GetMapping("/statistics")
    public Result<AttendanceStatisticsVO> statistics(AttendanceQueryDTO dto){
        return Result.ok(attendanceService.statistics(dto));
    }



}