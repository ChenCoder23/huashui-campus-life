package com.huashui.attendance.domain.vo;


import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;


@Data
public class AttendanceVO {


    private Long id;


    private Long workerId;


    private String workerName;


    private LocalDate attendanceDate;


    private LocalDateTime checkInTime;


    private String checkInType;


    private String checkInLocation;


    private String checkInPhoto;


    private String checkInStatus;


}