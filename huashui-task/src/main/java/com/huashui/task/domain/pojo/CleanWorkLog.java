package com.huashui.task.domain.pojo;
import com.baomidou.mybatisplus.annotation.*;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor; import lombok.Builder; import lombok.Data; import lombok.NoArgsConstructor;
import java.time.LocalDate; import java.time.LocalDateTime;
@Data @Builder @NoArgsConstructor @AllArgsConstructor
@TableName("clean_work_log") @Schema(description = "工作记录表")
public class CleanWorkLog {
    @TableId(type = IdType.AUTO) private Long id;
    private Long workerId;
    @Schema(description = "工作人员姓名（冗余）") private String workerName;
    private LocalDate workDate; private Long campusId; private Long buildingId;
    private String areaDesc; private String taskContent; private String status;
    private String image; private String remark;
    @TableField(fill = FieldFill.INSERT) private LocalDateTime createTime;
    @TableField(fill = FieldFill.INSERT_UPDATE) private LocalDateTime updateTime;
}