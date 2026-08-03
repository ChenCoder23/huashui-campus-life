package com.huashui.attendance.service;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.huashui.attendance.domain.pojo.AttendanceRecord;
public interface AttendanceRecordService extends IService<AttendanceRecord> {
    Page<AttendanceRecord> page(Integer page, Integer size, Long buildingId);
    void checkIn();
}