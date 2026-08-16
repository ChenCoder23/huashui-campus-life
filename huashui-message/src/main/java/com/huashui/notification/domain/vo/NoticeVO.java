package com.huashui.notification.domain.vo;

import com.huashui.notification.Enums.NoticeTopStatus;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 公告信息 VO
 */
@Data
public class NoticeVO {

    private Long id;

    private String title;

    private String content;

    private String noticeType;

    private LocalDateTime publishTime;

    private String summary;

    private String cover;

    private NoticeTopStatus isTop;
}
