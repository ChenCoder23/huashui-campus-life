package com.huashui.task.domain.pojo;
import com.baomidou.mybatisplus.annotation.*;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor; import lombok.Builder; import lombok.Data; import lombok.NoArgsConstructor;
import java.time.LocalDateTime;
@Data @Builder @NoArgsConstructor @AllArgsConstructor
@TableName("clean_task") @Schema(description = "工作任务表")
public class CleanTask {
    @TableId(type = IdType.AUTO) private Long id;
    private String taskName; private Long workerId;
    @Schema(description = "工作人员姓名（冗余）") private String workerName;
    private Long campusId; private Long buildingId; private String areaDesc;
    private LocalDateTime deadline; private String status;
    private LocalDateTime completedTime; private String completeImage; private String remark;
    private Long assignerId;
    @TableField(fill = FieldFill.INSERT) private LocalDateTime createTime;
    @TableField(fill = FieldFill.INSERT_UPDATE) private LocalDateTime updateTime;
}