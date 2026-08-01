package com.huashui.storage.domain.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 上传成功返回体
 * <p>
 * 前端直传拿到 url 存进业务表单（业务模块只存 url，不存 fileId）。
 * fileId 仅用于超管文件中心管理，业务链路不使用。
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "文件上传结果VO")
public class FileUploadVO {

    @Schema(description = "文件ID")
    private Long fileId;

    @Schema(description = "完整访问URL（业务模块存这个，可直接访问）")
    private String url;

    @Schema(description = "原始文件名")
    private String originalName;
}
