package com.huashui.attendance.domain.pojo;

import com.baomidou.mybatisplus.annotation.*;
import com.huashui.attendance.enums.AttendanceStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("attendance_record")
@Schema(description = "考勤记录表")
public class AttendanceRecord {

    @TableId(type = IdType.AUTO)
    private Long id;


    /**
     * 工作人员ID
     */
    private Long workerId;


    /**
     * 工作人员姓名快照
     */
    private String workerName;


    /**
     * 校区ID
     */
    private Long campusId;


    /**
     * 楼栋ID
     */
    private Long buildingId;


    /**
     * 考勤日期
     */
    private LocalDate attendanceDate;


    /**
     * 签到时间
     */
    private LocalDateTime checkInTime;


    /**
     * 签到方式
     */
    private String checkInType;


    /**
     * GPS位置
     */
    private String checkInLocation;


    /**
     * 签到照片
     */
    private String checkInPhoto;


    /**
     * 考勤状态
     */
    private AttendanceStatus checkInStatus;


    /**
     * 是否节假日
     */
    private Boolean isHoliday;


    private String remark;


    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;


    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
}