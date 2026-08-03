package com.huashui.announcement.domain.pojo;
import com.baomidou.mybatisplus.annotation.*;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor; import lombok.Builder; import lombok.Data; import lombok.NoArgsConstructor;
import java.time.LocalDateTime;
@Data @Builder @NoArgsConstructor @AllArgsConstructor @TableName("system_notice") @Schema(description = "系统公告表")
public class SystemNotice {
    @TableId(type = IdType.AUTO) private Long id;
    private String title; private String content; private String noticeType; private String attachment;
    private String pushScope; private String targetCampusIds; private String targetRoles; private String targetBuildingIds;
    private Boolean isTop; private Long publisherId;
    private LocalDateTime publishTime; private Integer viewCount; private String status;
    @TableField(fill = FieldFill.INSERT) private LocalDateTime createTime;
    @TableField(fill = FieldFill.INSERT_UPDATE) private LocalDateTime updateTime;
    @TableLogic private Integer isDeleted;
}