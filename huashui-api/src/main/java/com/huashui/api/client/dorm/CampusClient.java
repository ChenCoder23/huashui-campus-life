package com.huashui.api.client.dorm;

import com.huashui.common.response.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;
import java.util.Set;

@FeignClient("huashui-dormitory")
public interface CampusClient {

    @GetMapping("/campus/inner/batch-name")
    Result<Map<Long, String>> batchName(@RequestParam("ids") Set<Long> ids);
}