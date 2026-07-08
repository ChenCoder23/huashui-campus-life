package com.huashui.auth.util.test;

import cn.hutool.crypto.digest.BCrypt;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class BCryptTest {
    public static void main(String[] args) {

        //设置密码
        String password = "123456";

        String hash = BCrypt.hashpw(
                password,
                BCrypt.gensalt()
        );


        log.info("=====密码{}:加密后的结果为{}====",password,hash);

    }
}