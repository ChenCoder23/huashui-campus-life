package com.huashui.attendance.service.impl;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.huashui.attendance.domain.pojo.AttendanceRecord;
import com.huashui.attendance.mapper.AttendanceRecordMapper;
import com.huashui.attendance.service.AttendanceRecordService;
import com.huashui.common.utils.UserContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Slf4j @Service
public class AttendanceRecordServiceImpl extends ServiceImpl<AttendanceRecordMapper, AttendanceRecord> implements AttendanceRecordService {
    @Override
    public Page<AttendanceRecord> page(Integer page, Integer size, Long buildingId) {
        LambdaQueryWrapper<AttendanceRecord> qw = new LambdaQueryWrapper<>();
        String roles = UserContext.getRoles();
        if (roles != null && (roles.contains("CLEANER") || roles.contains("REPAIRER"))) qw.eq(AttendanceRecord::getWorkerId, UserContext.getUserId());
        if (buildingId != null) qw.eq(AttendanceRecord::getBuildingId, buildingId);
        qw.orderByDesc(AttendanceRecord::getAttendanceDate);
        return this.page(new Page<>(page, size), qw);
    }
    @Override @Transactional
    public void checkIn() {
        AttendanceRecord record = new AttendanceRecord();
        record.setWorkerId(UserContext.getUserId());
        record.setAttendanceDate(LocalDate.now());
        record.setCheckInTime(LocalDateTime.now());
        record.setCheckInStatus("NORMAL");
        save(record);
    }
}