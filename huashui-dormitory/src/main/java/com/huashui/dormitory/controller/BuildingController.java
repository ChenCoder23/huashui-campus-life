package com.huashui.dormitory.controller;

import com.huashui.common.response.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * 楼栋管理（含楼栋配置）
 * @author
 */
@RestController
@RequestMapping("/dormitory/building")
@RequiredArgsConstructor
@Tag(name = "楼栋管理")
public class BuildingController {

    /*
    *
    * 1	GET	/dormitory/building	超管/宿管	楼栋列表（分页，按校区筛选，宿管仅看管辖）
2	POST	/dormitory/building	超管	新增楼栋（含配置一起建）
3	PUT	/dormitory/building/{id}	超管	编辑楼栋
4	DELETE	/dormitory/building/{id}	超管	删除楼栋（有房间不可删,该接口针对手误创建的情况）
5	GET	/dormitory/building/{id}	超管/宿管	楼栋详情（含硬件配置）
6	PUT	/dormitory/building/{id}/config	超管	更新楼栋硬件配置
7	GET	/dormitory/building/options	登录即可	楼栋下拉（按校区筛选）
    *
    * */


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
    public Result<?> pageBuilding(
            @RequestParam(required = false) Long campusId,
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize) {


        return Result.success();
    }


    /**
     * 新增楼栋
     *
     * 创建楼栋时同时初始化硬件配置
     */
    @PostMapping
    @Operation(summary = "新增楼栋")
    public Result<Void> addBuilding(
            @RequestBody Object dto) {


        return Result.success();
    }


    /**
     * 编辑楼栋
     */
    @PutMapping("/{id}")
    @Operation(summary = "编辑楼栋")
    public Result<Void> updateBuilding(
            @PathVariable Long id,
            @RequestBody Object dto) {


        return Result.success();
    }


    /**
     * 删除楼栋
     *
     * 注意：
     * 存在房间时禁止删除
     */
    @DeleteMapping("/{id}")
    @Operation(summary = "删除楼栋")
    public Result<Void> deleteBuilding(
            @PathVariable Long id) {


        return Result.success();
    }


    /**
     * 楼栋详情
     *
     * 包含硬件配置
     */
    @GetMapping("/{id}")
    @Operation(summary = "楼栋详情")
    public Result<?> getBuildingDetail(
            @PathVariable Long id) {


        return Result.success();
    }


    /**
     * 更新楼栋硬件配置
     */
    @PutMapping("/{id}/config")
    @Operation(summary = "更新楼栋硬件配置")
    public Result<Void> updateBuildingConfig(
            @PathVariable Long id,
            @RequestBody Object dto) {


        return Result.success();
    }


    /**
     * 楼栋下拉选项
     *
     * 登录用户即可访问
     */
    @GetMapping("/options")
    @Operation(summary = "楼栋下拉列表")
    public Result<List<?>> getBuildingOptions(
            @RequestParam(required = false) Long campusId) {


        return Result.success();
    }


}
