package com.huashui.api.client.dorm;


import com.huashui.common.response.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;
import java.util.Set;


/**
 * 校区服务远程调用
 */
@FeignClient("huashui-dormitory")
public interface CampusClient {


    /**
     * 批量查询校区名称
     *
     * @param ids 校区ID集合
     * @return 校区ID-名称映射
     */
    @GetMapping("/campus/batch-name")
    Result<Map<Long, String>> batchName(@RequestParam("ids") Set<Long> ids);


}