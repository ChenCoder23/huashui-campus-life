package com.huashui.storage.domain.dto;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.huashui.common.domain.query.PageQuery;
import com.huashui.storage.Enum.BizType;
import com.huashui.storage.Enum.FileStatus;
import com.huashui.storage.domain.pojo.SysFile;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 * 文件中心分页查询DTO（超管 §5.5.13）
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Schema(description = "文件中心分页查询DTO")
public class FilePageDTO extends PageQuery {

    @Schema(description = "原始文件名（模糊查询）")
    private String originalName;

    @Schema(description = "业务类型")
    private BizType bizType;

    @Schema(description = "上传者用户ID")
    private Long uploaderId;

    @Schema(description = "状态（0-已删除，1-正常，空=全部）")
    private FileStatus status;

    @Schema(description = "上传时间-开始")
    private LocalDateTime beginTime;

    @Schema(description = "上传时间-结束")
    private LocalDateTime endTime;

    public Page<SysFile> toPage() {
        return new Page<>(this.getPageNum(), this.getPageSize());
    }
}
