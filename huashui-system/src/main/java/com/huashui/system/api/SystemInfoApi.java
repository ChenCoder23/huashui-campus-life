package com.huashui.system.api;

import com.huashui.common.response.Result;
import com.huashui.system.service.SysConfigService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author
 */

@RestController
@RequestMapping("/system/inner")
@RequiredArgsConstructor
public class SystemInfoApi {

    private final SysConfigService sysConfigService;


    @GetMapping("/config/value")
    Result<String> getConfigValue(@RequestParam String key){
        String value = sysConfigService.getValueByKey(key);

        return Result.ok(value);
    }
}
