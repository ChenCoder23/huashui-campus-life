package com.huashui.storage.domain.vo;

import com.huashui.storage.Enum.BizType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 文件元数据VO
 * <p>
 * 仅用于超管文件中心分页展示（业务模块直接存 url，不再调 storage 换取元数据）。
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "文件元数据VO")
public class FileVO {

    @Schema(description = "文件ID")
    private Long fileId;

    @Schema(description = "原始文件名")
    private String originalName;

    @Schema(description = "完整访问URL")
    private String url;

    @Schema(description = "文件大小（字节）")
    private Long fileSize;

    @Schema(description = "MIME类型")
    private String mimeType;

    @Schema(description = "扩展名")
    private String fileExt;

    @Schema(description = "业务类型")
    private BizType bizType;

    @Schema(description = "业务记录ID")
    private Long bizId;

    @Schema(description = "上传者用户ID")
    private Long uploaderId;

    @Schema(description = "上传时间")
    private LocalDateTime createTime;
}
