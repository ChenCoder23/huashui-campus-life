package com.huashui.dormitory.controller;

import com.huashui.common.response.PageResult;
import com.huashui.common.response.Result;
import com.huashui.dormitory.domain.dto.BuildingDTO;
import com.huashui.dormitory.domain.dto.BuildingPageDTO;
import com.huashui.dormitory.domain.vo.BuildingDetailVO;
import com.huashui.dormitory.domain.vo.BuildingPageVO;
import com.huashui.dormitory.service.DormBuildingService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 楼栋管理（含楼栋配置）
 * @author
 */
@RestController
@RequestMapping("/dormitory/building")
@RequiredArgsConstructor
@Tag(name = "楼栋管理")
public class BuildingController {

    @Autowired
    private DormBuildingService buildingService;


    /**
     * 楼栋列表
     *
     * 超管：
     *      查看全部楼栋
     *
     * 宿管：
     *      查看自己管辖楼栋
     */
    @GetMapping
    @Operation(summary = "楼栋分页列表")
    public Result<PageResult<BuildingPageVO>> pageBuilding(BuildingPageDTO dto){
        buildingService.getBuildingPage(dto);
        return Result.ok();


    }


    /**
     * 新增楼栋
     *
     * 创建楼栋时同时初始化硬件配置
     */
    @PostMapping
    @Operation(summary = "新增楼栋")
    public Result<Void> addBuilding(@RequestBody BuildingDTO dto) {
        buildingService.addBuilding(dto);
        return Result.ok();
    }


    /**
     * 编辑楼栋
     */
    @PutMapping
    @Operation(summary = "编辑楼栋")
    public Result<Void> updateBuilding(@RequestBody BuildingDTO dto) {
        buildingService.updateBuilding(dto);
        return Result.ok();
    }


    /**
     * 删除楼栋
     *
     * 注意：
     * 有人入住的状态下无法禁用
     */
    @DeleteMapping("/{id}")
    @Operation(summary = "禁用楼栋")
    public Result<Void> deleteBuilding(@PathVariable Long id) {
        return Result.ok();
    }


    /**
     * 楼栋详情
     *
     * 包含硬件配置
     */
    @GetMapping("/{id}")
    @Operation(summary = "楼栋详情")
    public Result<BuildingDetailVO> getBuildingDetail(@PathVariable Long id) {

        return Result.ok();
    }


    /**
     * 更新楼栋硬件配置
     */
    @PutMapping("/{id}/config")
    @Operation(summary = "更新楼栋硬件配置")
    public Result<Void> updateBuildingConfig(@PathVariable Long id, @RequestBody Object dto) {


        return Result.ok();
    }


    /**
     * 楼栋下拉选项
     *
     * 登录用户即可访问
     */
    @GetMapping("/options")
    @Operation(summary = "楼栋下拉列表")
    public Result<List<?>> getBuildingOptions(@RequestParam(required = false) Long campusId) {


        return Result.ok();
    }


}
