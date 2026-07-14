package com.huashui.common.constants;

/**
 * Redis 缓存 Key 常量
 */
public final class RedisConstants {

    private RedisConstants() {}

    /** 登录 Token */
    public static final String TOKEN_PREFIX = "token:";

    /** 验证码 */
    public static final String CAPTCHA_PREFIX = "captcha:code";


    /** 验证码请求次数 */
    public static final String CAPTCHA_PREFIX_NUM = "captcha:num:";

    /** 账号密码不匹配次数 */
    public static final String ACCOUNT_LOGIN_NUM = "account:login:num:";

    /** 短信验证码 */
    public static final String SMS_CODE_PREFIX = "sms:";

    /** 用户信息缓存 */
    public static final String USER_INFO_PREFIX = "user:info:";

    /** 黑名单 */
    public static final String BLACKLIST_KEY = "blacklist:ip";

    /** 限流 */
    public static final String RATE_LIMIT_PREFIX = "rate:";




    public static final Long CAPTCHA_EXPIRE = 10L;




}
