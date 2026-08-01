package com.huashui.dormitory.domain.pojo;

import com.baomidou.mybatisplus.annotation.*;
import com.huashui.common.domain.pojo.BaseEntity;
import com.huashui.dormitory.Enum.DormRecordStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("dorm_student_record")
@Schema(description = "学生住宿记录表")
public class DormStudentRecord extends BaseEntity {

    @TableId(type = IdType.AUTO)
    @Schema(description = "主键ID")
    private Long id;

    @Schema(description = "学生用户ID")
    private Long studentId;

    @Schema(description = "校区ID")
    private Long campusId;

    @Schema(description = "楼栋ID")
    private Long buildingId;

    @Schema(description = "房间ID")
    private Long roomId;

    @Schema(description = "床位ID")
    private Long bedId;

    @Schema(description = "入住时间")
    private LocalDateTime checkInTime;

    @Schema(description = "退宿时间")
    private LocalDateTime checkOutTime;

    @Schema(description = "住宿状态（0-已退宿，1-在住）")
    private DormRecordStatus status;


}