package com.huashui.attendance.service;
import com.baomidou.mybatisplus.extension.service.IService;
import com.huashui.attendance.domain.dto.AttendanceQueryDTO;
import com.huashui.attendance.domain.dto.AttendanceStatusUpdateDTO;
import com.huashui.attendance.domain.dto.CheckInDTO;
import com.huashui.attendance.domain.pojo.AttendanceRecord;
import com.huashui.attendance.domain.vo.AttendanceStatisticsVO;
import com.huashui.attendance.domain.vo.AttendanceVO;
import com.huashui.common.response.PageResult;

import java.util.List;

public interface AttendanceRecordService extends IService<AttendanceRecord> {


    void checkIn(CheckInDTO dto);

    AttendanceVO getTodayAttendance(Long workerId);

    AttendanceStatisticsVO statistics(AttendanceQueryDTO dto);

    List<AttendanceVO> getMyAttendance();

    PageResult<AttendanceVO> pageQuery(AttendanceQueryDTO dto);

    void updateAttendance(AttendanceStatusUpdateDTO dto);
}