package com.huashui.task.domain.vo.cleanTask;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;


/**
 * 保洁任务VO
 */
@Data
@Schema(description = "保洁任务详情")
public class CleanTaskDetailVO {


    @Schema(description = "任务ID")
    private Long id;


    @Schema(description = "保洁员ID")
    private Long workerId;


    @Schema(description = "保洁员姓名")
    private String workerName;


    @Schema(description = "校区ID")
    private Long campusId;


    @Schema(description = "校区名称")
    private String campusName;


    @Schema(description = "楼栋ID")
    private Long buildingId;


    @Schema(description = "楼栋名称")
    private String buildingName;


    @Schema(description = "任务日期")
    private LocalDate taskDate;


    @Schema(description = "任务类型")
    private String taskType;


    @Schema(description = "任务描述")
    private String description;


    @Schema(description = "任务状态")
    private Integer status;


    @Schema(description = "任务状态名称")
    private String statusName;


    @Schema(description = "开始时间")
    private LocalDateTime startTime;


    @Schema(description = "完成时间")
    private LocalDateTime completeTime;


    @Schema(description = "创建时间")
    private LocalDateTime createTime;

    @Schema(description = "任务完成的照片JSON数组")
    private String imageUrls;

}