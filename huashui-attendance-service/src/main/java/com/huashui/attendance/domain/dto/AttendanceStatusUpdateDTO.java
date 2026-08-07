package com.huashui.attendance.domain.dto;


import com.huashui.attendance.enums.AttendanceStatus;
import lombok.Data;


@Data
public class AttendanceStatusUpdateDTO {

    private Long workerId;


    private AttendanceStatus status;


    private String remark;

}