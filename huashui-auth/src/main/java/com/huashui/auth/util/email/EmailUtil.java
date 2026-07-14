package com.huashui.auth.util.email;

import cn.hutool.core.util.ReUtil;

/**
 * 判断是否为QQ邮箱
 */
public class EmailUtil {

    private static final String QQ_EMAIL_REGEX = "^[1-9][0-9]{4,11}@qq\\.com$";

    /**
     * 校验QQ邮箱格式
     *
     * @param email 邮箱
     * @return 是否是QQ邮箱
     */
    public static boolean isQQEmail(String email) {
        if (email == null || email.isBlank()) {
            return false;
        }

        return email.matches(QQ_EMAIL_REGEX);
    }
}