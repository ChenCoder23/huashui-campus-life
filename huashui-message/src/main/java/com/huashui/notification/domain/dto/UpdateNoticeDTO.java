package com.huashui.notification.domain.dto;

import com.huashui.notification.Enums.PushScope;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 修改公告 DTO
 */
@Data
public class UpdateNoticeDTO {

    private String title;

    private String content;

    private String noticeType;

    private String attachment;

    private PushScope pushScope;

    private List<Long> targetCampusIds;

    private List<String> targetRoles;

    private List<Long> targetBuildingIds;

    private Integer isTop;

    private LocalDateTime publishTime;
}
