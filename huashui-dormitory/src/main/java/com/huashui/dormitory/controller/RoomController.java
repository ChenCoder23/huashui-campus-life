package com.huashui.dormitory.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 房间管理（含床位管理）
 * @author
 */
@RestController
@RequestMapping("/dormitory/room")
@RequiredArgsConstructor
@Tag(name = "房间管理")
public class RoomController {
    /*
    *
    *
    * 1	GET	/dormitory/room	超管/宿管	房间列表（分页，按楼栋/楼层/状态筛选,宿舍管理只显示自己负责的楼栋）
2	POST	/dormitory/room	超管	新增房间（自动生成 N 个空床位）
3	PUT	/dormitory/room/{id}	超管	编辑房间
4	DELETE	/dormitory/room/{id}	超管	删除房间（有人住不可删）
5	GET	/dormitory/room/{id}	超管/宿管/学生	房间详情（含床位列表 + 入住学生）
6	POST	/dormitory/room/batch	超管	批量初始化房间（按楼层批量生成）
7	PUT	/dormitory/room/{id}/beds	超管	更新房间床位配置（增减床位）
8	GET	/dormitory/room/export	超管/宿管	导出房间数据为excel表格
    *
    * */


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


        return Result.success();
    }


    /**
     * 新增房间
     *
     * 自动生成N个空床位
     */
    @PostMapping
    @Operation(summary = "新增房间")
    public Result<Void> addRoom(
            @RequestBody Object dto) {


        return Result.success();
    }


    /**
     * 编辑房间
     */
    @PutMapping("/{id:\\d+}")
    @Operation(summary = "编辑房间")
    public Result<Void> updateRoom(
            @PathVariable Long id,
            @RequestBody Object dto) {


        return Result.success();
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


        return Result.success();
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


        return Result.success();
    }


    /**
     * 批量初始化房间
     *
     * 根据楼层批量生成房间
     */
    @PostMapping("/batch")
    @Operation(summary = "批量初始化房间")
    public Result<Void> batchCreateRoom(
            @RequestBody Object dto) {


        return Result.success();
    }


    /**
     * 更新房间床位配置
     *
     * 增加/减少床位
     */
    @PutMapping("/{id:\\d+}/beds")
    @Operation(summary = "更新房间床位配置")
    public Result<Void> updateRoomBeds(
            @PathVariable Long id,
            @RequestBody Object dto) {


        return Result.success();
    }


    /**
     * 导出房间数据
     */
    @GetMapping("/export")
    @Operation(summary = "导出房间数据")
    public void exportRoom() {


    }
}
