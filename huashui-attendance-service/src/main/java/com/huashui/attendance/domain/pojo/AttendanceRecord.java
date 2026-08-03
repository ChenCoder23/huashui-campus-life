package com.huashui.attendance.domain.pojo;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data @Builder @NoArgsConstructor @AllArgsConstructor
@TableName("attendance_record")
@Schema(description = "考勤记录表")
public class AttendanceRecord {
    @TableId(type = IdType.AUTO) private Long id;
    private Long workerId;
    @Schema(description = "工作人员姓名（冗余）") private String workerName;
    private Long campusId;
    private Long buildingId;
    private LocalDate attendanceDate;
    private LocalDateTime checkInTime;
    private String checkInType;
    private String checkInLocation;
    private String checkInPhoto;
    private String checkInStatus;
    private Boolean isHoliday;
    private String remark;
    @TableField(fill = FieldFill.INSERT) private LocalDateTime createTime;
    @TableField(fill = FieldFill.INSERT_UPDATE) private LocalDateTime updateTime;
}