package com.huashui.notification.domain.pojo;
import com.baomidou.mybatisplus.annotation.*;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor; import lombok.Builder; import lombok.Data; import lombok.NoArgsConstructor;
import java.time.LocalDateTime;
@Data @Builder @NoArgsConstructor @AllArgsConstructor @TableName("message") @Schema(description = "消息表")
public class Message {
    @TableId(type = IdType.AUTO) private Long id;
    private String type; private String title; private String content;
    private Long receiverId; private Long senderId;
    private String businessType; private Long businessId;
    private String status; private LocalDateTime readTime; private String priority;
    @TableField(fill = FieldFill.INSERT) private LocalDateTime createTime;
    @TableField(fill = FieldFill.INSERT_UPDATE) private LocalDateTime updateTime;
}