package com.huashui.evaluation.domain.pojo;
import com.baomidou.mybatisplus.annotation.*; import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor; import lombok.Builder; import lombok.Data; import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data @Builder @NoArgsConstructor @AllArgsConstructor @TableName("evaluation_result") @Schema(description = "EvaluationResult")
public class EvaluationResult {
    @TableId(type = IdType.AUTO) private Long id;
    private Long configId; private Long studentId; private String studentName; private Long campusId; private Long buildingId; private Long roomId; private String suggestion; private LocalDateTime submitTime; 
    @TableField(fill = FieldFill.INSERT) private LocalDateTime createTime;
    @TableField(fill = FieldFill.INSERT_UPDATE) private LocalDateTime updateTime;
}