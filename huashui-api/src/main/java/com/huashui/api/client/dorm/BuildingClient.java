package com.huashui.api.client.dorm;


import com.huashui.common.response.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;
import java.util.Set;


/**
 * 楼栋服务远程调用
 */
@FeignClient("huashui-dormitory")
public interface BuildingClient {


    /**
     * 批量查询楼栋名称
     *
     * @param ids 楼栋ID集合
     * @return 楼栋ID-名称映射
     */
    @GetMapping("/building/batch-name")
    Result<Map<Long, String>> batchName(
            @RequestParam("ids") Set<Long> ids
    );


    //根据管理员id获取负责的宿舍楼栋的id
    @GetMapping("/building/{budlingId}")
    Long getManagerId(@PathVariable Long budlingId);
}