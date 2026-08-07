package com.huashui.attendance.domain.dto;

import com.huashui.attendance.enums.AttendanceStatus;
import com.huashui.common.domain.query.PageQuery;
import lombok.Data;

import java.time.LocalDate;

@Data
public class AttendanceQueryDTO extends PageQuery {


    private Long workerId;


    private Long campusId;


    private Long buildingId;


    private AttendanceStatus status;


    private LocalDate startDate;


    private LocalDate endDate;



}