package com.huashui.auth.util.email;

import lombok.extern.slf4j.Slf4j;

import java.security.SecureRandom;
import java.time.LocalDate;

@Slf4j
public class VerifyCodeUtil {

    private static final SecureRandom RANDOM = new SecureRandom();

    /**
     * 生成6位数字验证码
     */
    public static String generateCode() {
        int code = RANDOM.nextInt(900000) + 100000;
        log.info("[{}]生成验证码:{}", LocalDate.now(),code);// 保证是6位数，不会有前导0缺失问题
        return String.valueOf(code);
    }
}