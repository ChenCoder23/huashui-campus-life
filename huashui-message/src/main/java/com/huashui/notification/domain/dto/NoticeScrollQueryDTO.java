package com.huashui.notification.domain.dto;

import lombok.Data;

@Data
public class NoticeScrollQueryDTO {

    /**
     * 游标
     *
     * 第一次查询为空，
     * 后续查询传递上一次返回的 nextCursor
     */
    private Long cursor;

    /**
     * 每页数量
     */
    private Integer pageSize = 10;
}