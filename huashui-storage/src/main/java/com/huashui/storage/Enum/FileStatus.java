package com.huashui.storage.Enum;

import com.baomidou.mybatisplus.annotation.EnumValue;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 文件状态枚举（逻辑删除标记）
 */
@Getter
@AllArgsConstructor
@Schema(description = "文件状态")
public enum FileStatus {

    @Schema(description = "已删除")
    DELETED(0, "已删除"),

    @Schema(description = "正常")
    NORMAL(1, "正常");

    @EnumValue
    private final Integer code;

    private final String desc;
}
