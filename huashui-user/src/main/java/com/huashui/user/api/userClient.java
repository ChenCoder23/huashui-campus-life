package com.huashui.user.api;

import com.huashui.common.domain.dto.UserSimpleInfo;
import com.huashui.common.enums.LoginType;
import com.huashui.user.service.SysUserService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

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





    // 根据Id列表查询用户信息
    @GetMapping("/list")
    public List<UserSimpleInfo> getUserInfoList(@RequestParam List<Long> userIds) {
        return userService.getUserInfoList(userIds);
    }

    // 更新用户头像
    @PutMapping("/avatar")
    public void updateAvatar(@RequestParam Long userId, @RequestParam String avatarUrl) {
        userService.updateAvatar(userId, avatarUrl);
    }

    //更新用户密码
    @GetMapping("/password")
    void updatePassword(@RequestParam Long id,@RequestParam String password){
        userService.updatePassword(id,password);
    }


}