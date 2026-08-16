package com.huashui.notification.domain.vo;

import lombok.Data;

import java.util.List;

@Data
public class ScrollVO<T> {

    /**
     * 消息列表
     */
    private List<T> records;

    /**
     * 下一页游标
     */
    private Long nextCursor;

    /**
     * 是否还有更多
     */
    private Boolean hasMore;
}