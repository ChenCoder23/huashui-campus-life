package com.huashui.api.client.system;

import com.huashui.common.response.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient("huashui-system")
public interface SystemClient {


    /**
     * 获取系统配置
     */
    @GetMapping("/system/config/value")
    Result<String> getConfigValue(@RequestParam("key") String key);

}