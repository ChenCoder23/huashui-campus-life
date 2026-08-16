package com.huashui.dormitory.api;

import com.huashui.common.response.Result;
import com.huashui.dormitory.service.SysCampusService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import java.util.Set;

@RestController
@RequestMapping("/campus/inner")
@RequiredArgsConstructor
public class campusAPI {


    private final SysCampusService campusService;



    /**
     * 批量查询校区名称
     */
    @GetMapping("/batch-name")
    public Result<Map<Long,String>> batchName(@RequestParam("ids") Set<Long> ids){

        return Result.ok(campusService.batchName(ids));

    }

}