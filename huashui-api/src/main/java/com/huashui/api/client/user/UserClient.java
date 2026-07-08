package com.huashui.api.client.user;

import com.huashui.api.fallback.UserClientFallbackFactory;
import com.huashui.common.domain.dto.UserSimpleInfo;
import com.huashui.common.enums.LoginType;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name="huashui-user",fallbackFactory = UserClientFallbackFactory.class)
public interface UserClient {


    @GetMapping("/user/inner/userInfo")
    UserSimpleInfo getUserInfo(@RequestParam String account , @RequestParam LoginType type);

    //根据用户id跟新登录时间
    @GetMapping("/loginTime")
    public void updateLoginTime(Long id);


}