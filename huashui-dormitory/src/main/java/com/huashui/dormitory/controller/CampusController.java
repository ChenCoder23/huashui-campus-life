package com.huashui.dormitory.controller;

import com.huashui.common.response.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 校区管理
 *
 * @author
 */
@RestController
@RequestMapping("/dormitory/campus")
@RequiredArgsConstructor
@Tag(name = "校区管理")
public class CampusController {

    /*
    *
    *
    * 1	GET	/dormitory/campus	超管	校区列表（分页）
2	POST	/dormitory/campus	超管	新增校区
3	PUT	/dormitory/campus/{id}	超管	编辑校区
4	DELETE	/dormitory/campus/{id}	超管	删除校区（关联楼栋的不可删）
5	GET	/dormitory/campus/options	登录即可	校区下拉选项（其他页面筛选用）*/

    /**
     * 校区分页列表
     *
     * 权限：
     * 超级管理员
     */
    @GetMapping
    @Operation(summary = "分页查询校区列表")
    public Result<?> pageCampus(
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize) {


        return Result.success();
    }


    /**
     * 新增校区
     */
    @PostMapping
    @Operation(summary = "新增校区")
    public Result<Void> addCampus(
            @RequestBody Object dto) {


        return Result.success();
    }


    /**
     * 编辑校区
     */
    @PutMapping("/{id:\\d+}")
    @Operation(summary = "编辑校区")
    public Result<Void> updateCampus(
            @PathVariable Long id,
            @RequestBody Object dto) {


        return Result.success();
    }


    /**
     * 删除校区
     *
     * 关联楼栋时禁止删除
     */
    @DeleteMapping("/{id:\\d+}")
    @Operation(summary = "删除校区")
    public Result<Void> deleteCampus(
            @PathVariable Long id) {


        return Result.success();
    }


    /**
     * 校区下拉选项
     *
     * 登录用户即可访问
     */
    @GetMapping("/options")
    @Operation(summary = "获取校区下拉选项")
    public Result<List<?>> getCampusOptions() {


        return Result.success();
    }



}
