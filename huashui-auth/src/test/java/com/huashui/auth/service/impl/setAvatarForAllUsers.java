package com.huashui.auth.service.impl;

import com.huashui.auth.service.SysUserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * @author
 */
@SpringBootTest
public class setAvatarForAllUsers {

    @Autowired
    private SysUserService userService;


    @Test
    public void setAvatar(){
        // todo 为所有用户设置头像
    }
}
