package com.huashui.notification.domain.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import com.huashui.notification.Enums.NoticeStatus;
import com.huashui.notification.Enums.NoticeTopStatus;
import com.huashui.notification.Enums.PushScope;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 系统公告
 */
@Data
@TableName("system_notice")
public class SystemNotice {

    /**
     * 主键ID
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 公告标题
     */
    private String title;

    /**
     * 公告正文
     */
    private String content;

    /**
     * 公告类型
     */
    private String noticeType;

    /**
     * 附件URL
     */
    private String attachment;

    /**
     * 推送范围
     */
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
    private NoticeTopStatus isTop;

    /**
     * 发布人ID
     */
    private Long publisherId;

    /**
     * 发布时间
     */
    private LocalDateTime publishTime;

    /**
     * 总浏览量
     */
    private Integer viewCount;

    /**
     * 状态
     */
    private NoticeStatus status;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;

    /**
     * 逻辑删除
     */
    @TableLogic
    private Integer isDeleted;
}
