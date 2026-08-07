package com.huashui.api.client.user;

import com.huashui.api.domain.vo.CleanerSimpleVO;
import com.huashui.api.fallback.UserClientFallbackFactory;
import com.huashui.common.domain.dto.UserSimpleInfo;
import com.huashui.common.enums.LoginType;
import com.huashui.common.response.Result;
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



    // 根据Id列表查询用户信息
    @GetMapping("user/inner/list")
    public List<UserSimpleInfo> getUserInfoList(@RequestParam List<Long> userIds) ;

    /**
     * 根据角色查询用户
     *
     * @param role 角色编码
     */
    @GetMapping("/user/listByRole")
    Result<List<CleanerSimpleVO>> listByRole(@RequestParam("role") String role);
}