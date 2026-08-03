package com.huashui.utility.domain.pojo;
import com.baomidou.mybatisplus.annotation.*;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor; import lombok.Builder; import lombok.Data; import lombok.NoArgsConstructor;
import java.math.BigDecimal; import java.time.*;
@Data @Builder @NoArgsConstructor @AllArgsConstructor @TableName("payment_order") @Schema(description = "PaymentOrder")
public class PaymentOrder {
    @TableId(type = IdType.AUTO) private Long id;
    private String orderNo; private Long userId; private Long roomId; private String paymentType; private BigDecimal amount; private String payMethod; private String status; private String transactionId; private LocalDateTime paidTime; private LocalDateTime refundTime; private String refundReason; 
    @TableField(fill = FieldFill.INSERT) private LocalDateTime createTime;
    @TableField(fill = FieldFill.INSERT_UPDATE) private LocalDateTime updateTime;
}