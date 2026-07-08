package com.huashui.user.api;

import com.huashui.common.domain.dto.UserSimpleInfo;
import com.huashui.common.enums.LoginType;
import com.huashui.user.domain.pojo.SysUser;
import com.huashui.user.service.SysUserService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user/inner")
public class userClient {


    @Resource
    private SysUserService userService;


    // 根据手机号/邮箱/账号(学号/工号)查询用户信息
    @GetMapping("/userInfo")
    public UserSimpleInfo getUserInfo(
            @RequestParam String account ,
            @RequestParam LoginType type){
        return userService.getUserInfoByAccount(account, type);
    }



}