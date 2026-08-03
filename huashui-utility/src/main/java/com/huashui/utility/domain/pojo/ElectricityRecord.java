package com.huashui.utility.domain.pojo;
import com.baomidou.mybatisplus.annotation.*;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor; import lombok.Builder; import lombok.Data; import lombok.NoArgsConstructor;
import java.math.BigDecimal; import java.time.*;
@Data @Builder @NoArgsConstructor @AllArgsConstructor @TableName("electricity_record") @Schema(description = "ElectricityRecord")
public class ElectricityRecord {
    @TableId(type = IdType.AUTO) private Long id;
    private Long roomId; private LocalDate recordDate; private BigDecimal usageAmount; private BigDecimal unitPrice; private BigDecimal amount; private BigDecimal balanceBefore; private BigDecimal balanceAfter; 
    @TableField(fill = FieldFill.INSERT) private LocalDateTime createTime;
    @TableField(fill = FieldFill.INSERT_UPDATE) private LocalDateTime updateTime;
}