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


    /**
     * 校区分页列表
     *
     * 权限：
     * 超级管理员
     */
    @GetMapping
    @Operation(summary = "查询校区列表")
    public Result<?> pageCampus() {
        return Result.ok();
    }


    /**
     * 新增校区
     */
    @PostMapping
    @Operation(summary = "新增校区")
    public Result<Void> addCampus(@RequestBody Object dto) {
        return Result.ok();
    }


    /**
     * 编辑校区
     */
    @PutMapping
    @Operation(summary = "编辑校区")
    public Result<Void> updateCampus(@RequestBody Object dto) {
        return Result.ok();
    }


    /**
     * 删除校区
     *
     * 关联楼栋时禁止删除
     */
    @DeleteMapping("/{id}")
    @Operation(summary = "停用校区") // 判断是否该校区的每一个楼栋全部停用
    public Result<Void> deleteCampus(@PathVariable Long id) {
        return Result.ok();
    }


    /**
     * 校区下拉选项
     *
     * 登录用户即可访问
     */
    @GetMapping("/options")
    @Operation(summary = "获取校区下拉选项")
    public Result<List<?>> getCampusOptions() {
        return Result.ok();
    }



}
