package com.huashui.api.client.user;

import com.huashui.api.domain.vo.task.CleanerSimpleVO;
import com.huashui.api.fallback.UserClientFallbackFactory;
import com.huashui.common.domain.dto.UserSimpleInfo;
import com.huashui.common.enums.LoginType;
import com.huashui.common.response.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(name="huashui-auth", fallbackFactory = UserClientFallbackFactory.class)
public interface UserClient {

    @GetMapping("/user/inner/userInfo")
    UserSimpleInfo getUserInfo(@RequestParam String account, @RequestParam LoginType type);

    @GetMapping("/user/inner/list")
    List<UserSimpleInfo> getUserInfoList(@RequestParam List<Long> userIds);

    @GetMapping("/user/inner/user/listByRole")
    Result<List<CleanerSimpleVO>> listByRole(@RequestParam("role") String role);

    @GetMapping("/user/inner/{id}")
    UserSimpleInfo getUserInfoById(@PathVariable("id") Long id);
}