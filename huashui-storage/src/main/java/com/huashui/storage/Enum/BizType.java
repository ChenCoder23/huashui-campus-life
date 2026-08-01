package com.huashui.storage.Enum;

import com.baomidou.mybatisplus.annotation.EnumValue;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;
import java.util.Set;

/**
 * 文件业务类型枚举
 * <p>
 * 每种业务类型自带：允许的扩展名白名单 + 单文件大小上限（字节）。
 * 上传时按 bizType 做校验，避免把校验规则散落在 Service 里。
 */
@Getter
@AllArgsConstructor
@Schema(description = "文件业务类型")
public enum BizType {

    @Schema(description = "用户头像")
    AVATAR("AVATAR", "用户头像", Set.of("jpg", "jpeg", "png", "webp"), 5L * 1024 * 1024),

    @Schema(description = "请假证明")
    LEAVE_PROOF("LEAVE_PROOF", "请假证明", Set.of("jpg", "jpeg", "png", "webp", "pdf"), 10L * 1024 * 1024),

    @Schema(description = "报修图片")
    REPAIR_IMAGE("REPAIR_IMAGE", "报修图片", Set.of("jpg", "jpeg", "png", "webp"), 10L * 1024 * 1024),

    @Schema(description = "考勤图片")
    ATTENDANCE("ATTENDANCE", "考勤图片", Set.of("jpg", "jpeg", "png", "webp"), 10L * 1024 * 1024),

    @Schema(description = "卫生任务图片")
    CLEAN_TASK("CLEAN_TASK", "卫生任务图片", Set.of("jpg", "jpeg", "png", "webp"), 10L * 1024 * 1024),

    @Schema(description = "工作日志附件")
    WORK_LOG("WORK_LOG", "工作日志附件", Set.of("jpg", "jpeg", "png", "webp", "pdf"), 10L * 1024 * 1024),

    @Schema(description = "公告附件")
    NOTICE("NOTICE", "公告附件", Set.of("jpg", "jpeg", "png", "webp", "pdf"), 20L * 1024 * 1024);

    /**
     * 业务码，与数据库 biz_type 字段一致
     */
    @EnumValue
    private final String code;

    /**
     * 描述
     */
    private final String desc;

    /**
     * 允许上传的扩展名白名单（小写，不含点）
     */
    private final Set<String> allowExt;

    /**
     * 单文件大小上限（字节）
     */
    private final Long maxSize;

    /**
     * 校验扩展名是否在白名单内
     *
     * @param ext 扩展名（小写，不含点）
     */
    public boolean allowExt(String ext) {
        return ext != null && this.allowExt.contains(ext.toLowerCase());
    }

    /**
     * 校验文件大小是否超限
     *
     * @param size 文件大小（字节）
     */
    public boolean allowSize(long size) {
        return size > 0 && size <= this.maxSize;
    }

    /**
     * 根据 code 解析枚举，非法值返回 null
     */
    public static BizType of(String code) {
        return Arrays.stream(values())
                .filter(t -> t.code.equalsIgnoreCase(code))
                .findFirst()
                .orElse(null);
    }
}
