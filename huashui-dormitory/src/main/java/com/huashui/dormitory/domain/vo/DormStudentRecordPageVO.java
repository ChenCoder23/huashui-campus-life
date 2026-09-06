package com.huashui.dormitory.domain.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Schema(description = "住宿记录分页返回VO")
public class DormStudentRecordPageVO {

    @Schema(description = "学生姓名")
    private String studentName;

    @Schema(description = "校区名称")
    private String campusName;

    @Schema(description = "楼栋名称")
    private String buildingName;

    @Schema(description = "房间号")
    private String roomNumber;

    @Schema(description = "床位号")
    private String bedNumber;

    @Schema(description = "学期")
    private String semester;

    @Schema(description = "入住时间")
    private LocalDateTime checkInTime;

    @Schema(description = "退宿时间")
    private LocalDateTime checkOutTime;

    @Schema(description = "住宿状态")
    private String status;
}
