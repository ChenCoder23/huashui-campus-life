package com.huashui.attendance.mapper;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.huashui.attendance.domain.pojo.AttendanceRecord;
import org.apache.ibatis.annotations.Mapper;

import java.time.LocalDate;
import java.util.List;

@Mapper
public interface AttendanceRecordMapper extends BaseMapper<AttendanceRecord> {

    @Select("""
            SELECT worker_id
            FROM attendance_record
            WHERE attendance_date = #{attendanceDate}
            """)
    List<Long> selectExistingWorkerIds(@Param("attendanceDate") LocalDate attendanceDate);
}
