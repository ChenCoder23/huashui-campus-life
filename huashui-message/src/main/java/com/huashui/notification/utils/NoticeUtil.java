package com.huashui.notification.utils;

import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HtmlUtil;


/**
 * 公告工具类
 */
public class NoticeUtil {

    private NoticeUtil() {
    }

    /**
     * 根据公告正文生成简介
     *
     * <p>处理逻辑：
     * <ol>
     *     <li>去除 HTML 标签</li>
     *     <li>去除首尾空白字符</li>
     *     <li>截取前 maxLength 个字符</li>
     *     <li>超过长度时追加省略号</li>
     * </ol>
     *
     * @param content   公告正文
     * @param maxLength 最大字符数
     * @return 公告简介
     */
    public static String buildSummary(String content, int maxLength) {

        if (StrUtil.isBlank(content)) {
            return "";
        }

        if (maxLength <= 0) {
            return "";
        }

        // 去除 HTML 标签
        String text = HtmlUtil.cleanHtmlTag(content);

        // 去除首尾空白
        text = StrUtil.trim(text);

        if (text.length() <= maxLength) {
            return text;
        }

        return StrUtil.subPre(text, maxLength) + "...";
    }
}