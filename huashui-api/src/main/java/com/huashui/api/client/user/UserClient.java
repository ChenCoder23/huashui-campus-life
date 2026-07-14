package com.huashui.api.client.user;

import com.huashui.api.fallback.UserClientFallbackFactory;
import com.huashui.common.domain.dto.UserSimpleInfo;
import com.huashui.common.enums.LoginType;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(name="huashui-user",fallbackFactory = UserClientFallbackFactory.class)
public interface UserClient {


    //根据账号获取用户的基本信息
    @GetMapping("/user/inner/userInfo")
    UserSimpleInfo getUserInfo(@RequestParam String account , @RequestParam LoginType type);

    //根据用户id更新登录时间
    @GetMapping("user/inner/loginTime")
    public void updateLoginTime(Long id);

    // 更新用户头像
    @PutMapping("/user/inner/avatar")
    void updateAvatar(@RequestParam Long userId, @RequestParam String avatarUrl);


    // 根据Id列表查询用户信息
    @GetMapping("user/inner/list")
    public List<UserSimpleInfo> getUserInfoList(@RequestParam List<Long> userIds) ;


    @GetMapping("user/inner/password")
    void updatePassword(@RequestParam Long id,@RequestParam String password);
}