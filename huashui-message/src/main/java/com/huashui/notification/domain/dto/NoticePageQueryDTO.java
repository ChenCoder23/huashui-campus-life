package com.huashui.notification.domain.dto;

import lombok.Data;

/**
 * 公告分页查询 DTO
 */
@Data
public class NoticePageQueryDTO {

    /**
     * 页码
     */
    private Integer pageNum = 1;

    /**
     * 每页数量
     */
    private Integer pageSize = 10;

    /**
     * 公告标题
     */
    private String title;

    /**
     * 公告类型
     */
    private String noticeType;
}