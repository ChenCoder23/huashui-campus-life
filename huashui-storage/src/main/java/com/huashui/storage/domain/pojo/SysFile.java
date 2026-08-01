package com.huashui.storage.domain.pojo;

import com.baomidou.mybatisplus.annotation.*;
import com.huashui.storage.Enum.BizType;
import com.huashui.storage.Enum.FileStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("sys_file")
@Schema(description = "文件元数据表")
public class SysFile {

    @TableId(type = IdType.AUTO)
    @Schema(description = "主键ID")
    private Long id;

    @Schema(description = "原始文件名")
    private String originalName;

    @Schema(description = "MinIO存储路径（bizType/yyyy/MM/uuid.ext）")
    private String objectName;

    @Schema(description = "访问URL（完整可直接访问的URL，业务模块直接存这个）")
    private String accessUrl;

    @Schema(description = "文件大小（字节）")
    private Long fileSize;

    @Schema(description = "MIME类型")
    private String mimeType;

    @Schema(description = "扩展名（小写，不含点）")
    private String fileExt;

    @Schema(description = "SHA-256（本期不做去重，仅记录）")
    private String fileHash;

    @Schema(description = "业务类型")
    private BizType bizType;

    @Schema(description = "业务记录ID（先传后绑，允许为空）")
    private Long bizId;

    @Schema(description = "上传者用户ID")
    private Long uploaderId;

    @Schema(description = "状态（0-已删除，1-正常）")
    private FileStatus status;

    @TableField(fill = FieldFill.INSERT)
    @Schema(description = "上传时间")
    private LocalDateTime createTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    @Schema(description = "更新时间")
    private LocalDateTime updateTime;
}
