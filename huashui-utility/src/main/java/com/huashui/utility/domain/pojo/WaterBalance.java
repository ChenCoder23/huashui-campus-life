package com.huashui.utility.domain.pojo;
import com.baomidou.mybatisplus.annotation.*;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor; import lombok.Builder; import lombok.Data; import lombok.NoArgsConstructor;
import java.math.BigDecimal; import java.time.*;
@Data @Builder @NoArgsConstructor @AllArgsConstructor @TableName("water_balance") @Schema(description = "WaterBalance")
public class WaterBalance {
    @TableId(type = IdType.AUTO) private Long id;
    private Long roomId; private BigDecimal balance; private BigDecimal freeQuota; private BigDecimal totalUsage; private Integer status; private LocalDateTime stoppedTime; private LocalDateTime restoredTime; 
    @TableField(fill = FieldFill.INSERT) private LocalDateTime createTime;
    @TableField(fill = FieldFill.INSERT_UPDATE) private LocalDateTime updateTime;
}