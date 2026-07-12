package com.huashui.dormitory.controller;

import com.huashui.common.response.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * 房间管理（含床位管理）
 * @author
 */
@RestController
@RequestMapping("/dormitory/room")
@RequiredArgsConstructor
@Tag(name = "房间管理")
public class RoomController {



    /**
     * 房间分页列表
     *
     * 支持：
     * 1. 楼栋筛选
     * 2. 楼层筛选
     * 3. 房间状态筛选
     */
    @GetMapping
    @Operation(summary = "分页查询房间列表")
    public Result<?> pageRoom(
            @RequestParam(required = false) Long buildingId,
            @RequestParam(required = false) Integer floor,
            @RequestParam(required = false) Integer status,
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize) {
        //做缓存
        return Result.ok();
    }


    /**
     * 新增房间
     *
     * 自动生成N个空床位
     */
    @PostMapping
    @Operation(summary = "新增房间")
    public Result<Void> addRoom(@RequestBody Object dto) {


        return Result.ok();
    }


    /**
     * 编辑房间
     */
    @PutMapping("/{id}")
    @Operation(summary = "编辑房间")
    public Result<Void> updateRoom(@PathVariable Long id, @RequestBody Object dto) {


        return Result.ok();
    }


    /**
     * 删除房间
     *
     * 有学生入住禁止删除
     */
    @DeleteMapping("/{id:\\d+}")
    @Operation(summary = "删除房间")
    public Result<Void> deleteRoom(
            @PathVariable Long id) {


        return Result.ok();
    }


    /**
     * 房间详情
     *
     * 包含：
     * 1. 床位列表
     * 2. 入住学生
     */
    @GetMapping("/{id:\\d+}")
    @Operation(summary = "查询房间详情")
    public Result<?> getRoomDetail(
            @PathVariable Long id) {


        return Result.ok();
    }


    /**
     * 批量初始化房间
     *
     * 根据楼层批量生成房间
     */
    @PostMapping("/batch")
    @Operation(summary = "批量初始化房间")
    public Result<Void> batchCreateRoom(@RequestBody Object dto) {


        return Result.ok();
    }


    /**
     * 更新房间床位配置
     *
     * 增加/减少床位
     */
    @PutMapping("/{id}/beds")
    @Operation(summary = "更新房间床位配置")
    public Result<Void> updateRoomBeds(
            @PathVariable Long id,
            @RequestBody Object dto) {


        return Result.ok();
    }


    /**
     * 导出房间数据
     */
    @GetMapping("/export")
    @Operation(summary = "导出房间数据")
    public void exportRoom() {


    }
}
