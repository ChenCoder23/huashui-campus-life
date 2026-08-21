package com.huashui.api.client.dorm;

import com.huashui.common.response.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;
import java.util.Set;

@FeignClient(name =  "huashui-dormitory", contextId = "BuildingClient")
public interface BuildingClient {

    @GetMapping("/building/inner/batch-name")
    Result<Map<Long, String>> batchName(@RequestParam("ids") Set<Long> ids);

    @GetMapping("/building/inner/building/{budlingId}")
    Long getManagerId(@PathVariable Long budlingId);
}