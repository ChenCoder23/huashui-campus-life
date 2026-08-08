package com.huashui.dormitory.api;

import com.huashui.common.response.Result;
import com.huashui.dormitory.domain.pojo.DormBuildingManager;
import com.huashui.dormitory.service.DormBuildingService;
import com.huashui.dormitory.service.IDormBuildingManagerService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Set;

@RestController
@RequestMapping("/building/inner")
@RequiredArgsConstructor
public class BuildingController {


    private final DormBuildingService buildingService;

    private final IDormBuildingManagerService buildingManagerService;

    /**
     * 批量查询楼栋名称
     */
    @GetMapping("/batch-name")
    public Result<Map<Long,String>> batchName(@RequestParam("ids") Set<Long> ids){
        return Result.ok(buildingService.batchName(ids));
    }


    //根据楼栋id,查询宿舍管理员的id
    @GetMapping("/building/{budlingId}")
    public Long getManagerId(@PathVariable Long budlingId){
     return buildingManagerService.lambdaQuery().eq(DormBuildingManager::getBuildingId,budlingId).one().getUserId();
    }
}