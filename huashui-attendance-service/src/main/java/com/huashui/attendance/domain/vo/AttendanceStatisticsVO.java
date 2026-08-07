package com.huashui.attendance.domain.vo;


import lombok.Data;


@Data
public class AttendanceStatisticsVO {


    private Integer total;


    private Integer normal;


    private Integer late;


    private Integer absent;


    private Integer leave;


}