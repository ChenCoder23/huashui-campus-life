package com.huashui.notification.domain.dto;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import com.huashui.notification.Enums.PushScope;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 创建公告 DTO
 */
@Data
public class CreateNoticeDTO {

    /**
     * 公告标题
     */
    @NotBlank(message = "公告标题不能为空")
    private String title;

    /**
     * 公告正文
     */
    @NotBlank(message = "公告正文不能为空")
    private String content;

    /**
     * 公告类型
     *
     * WATER_STOP
     * POWER_STOP
     * HOLIDAY
     * SAFETY_CHECK
     * AC_RENTAL
     * OTHER
     */
    @NotBlank(message = "公告类型不能为空")
    private String noticeType;

    /**
     * 公告附件/封面
     */
    private String attachment;

    /**
     * 推送范围
     *
     * ALL
     * CAMPUS
     * ROLE
     * BUILDING
     */
    @NotBlank(message = "推送范围不能为空")
    private PushScope pushScope;


    /**
     * 目标校区ID数组
     */
    @TableField(typeHandler = JacksonTypeHandler.class)
    private List<Long> targetCampusIds;

    /**
     * 目标角色数组
     */
    @TableField(typeHandler = JacksonTypeHandler.class)
    private List<String> targetRoles;

    /**
     * 目标楼栋ID数组
     */
    @TableField(typeHandler = JacksonTypeHandler.class)
    private List<Long> targetBuildingIds;

    /**
     * 是否置顶
     */
    @NotNull(message = "是否置顶不能为空")
    private Integer isTop;

    /**
     * 发布时间
     */
    @NotNull(message = "发布时间不能为空")
    private LocalDateTime publishTime;
}