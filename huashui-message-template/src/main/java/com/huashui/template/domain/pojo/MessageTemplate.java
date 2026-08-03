package com.huashui.template.domain.pojo;
import com.baomidou.mybatisplus.annotation.*;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor; import lombok.Builder; import lombok.Data; import lombok.NoArgsConstructor;
import java.time.LocalDateTime;
@Data @Builder @NoArgsConstructor @AllArgsConstructor @TableName("message_template") @Schema(description = "消息模板表")
public class MessageTemplate {
    @TableId(type = IdType.AUTO) private Long id;
    private String templateName; private String templateCode; private String templateContent;
    private String type; private String status;
    @TableField(fill = FieldFill.INSERT) private LocalDateTime createTime;
    @TableField(fill = FieldFill.INSERT_UPDATE) private LocalDateTime updateTime;
}