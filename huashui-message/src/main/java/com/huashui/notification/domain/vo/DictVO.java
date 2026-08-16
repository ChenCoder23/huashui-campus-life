package com.huashui.notification.domain.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 字典项 VO
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DictVO {

    /**
     * 显示标签
     */
    private String label;

    /**
     * 实际值
     */
    private String value;
}
