package com.huashui.evaluation.domain.pojo;
import com.baomidou.mybatisplus.annotation.*; import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor; import lombok.Builder; import lombok.Data; import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data @Builder @NoArgsConstructor @AllArgsConstructor @TableName("evaluation_config") @Schema(description = "EvaluationConfig")
public class EvaluationConfig {
    @TableId(type = IdType.AUTO) private Long id;
    private String configName; private Long campusId; private String buildingIds; private LocalDateTime startTime; private LocalDateTime endTime; private String status; private Long creatorId; 
    @TableField(fill = FieldFill.INSERT) private LocalDateTime createTime;
    @TableField(fill = FieldFill.INSERT_UPDATE) private LocalDateTime updateTime;
}