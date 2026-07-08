package com.huashui.common.domain.vo;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class PageVO<T> implements Serializable {

    private Long total;        // 总记录数
    private Long pageNum;      // 当前页
    private Long pageSize;     // 每页大小
    private List<T> records;   // 数据列表
}