package com.huashui.dormitory.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.huashui.common.response.PageResult;
import com.huashui.common.response.Result;
import com.huashui.dormitory.domain.dto.RoomBatchCreateDTO;
import com.huashui.dormitory.domain.dto.RoomCreateDTO;
import com.huashui.dormitory.domain.dto.RoomPageDTO;
import com.huashui.dormitory.domain.dto.RoomUpdateDTO;
import com.huashui.dormitory.domain.pojo.DormRoom;
import com.huashui.dormitory.service.DormRoomService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/dormitory/room")
@RequiredArgsConstructor
@Tag(name = "房间管理")
public class RoomController {

    private final DormRoomService roomService;

    @GetMapping
    @Operation(summary = "房间列表（分页）")
    public Result<PageResult<DormRoom>> list(RoomPageDTO dto) {
        return Result.ok(roomService.getRoomPage(dto));
    }

    @PostMapping
    @Operation(summary = "新增房间（自动生成空床位）")
    public Result<Void> create(@Valid @RequestBody RoomCreateDTO dto) {
        roomService.create(dto);
        return Result.ok();
    }

    @PutMapping("/{id}")
    @Operation(summary = "编辑房间")
    public Result<Void> update(@PathVariable Long id, @Valid @RequestBody RoomUpdateDTO dto) {
        roomService.update(id, dto);
        return Result.ok();
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "删除房间")
    public Result<Void> delete(@PathVariable Long id) {
        roomService.deleteById(id);
        return Result.ok();
    }

    @GetMapping("/{id}")
    @Operation(summary = "房间详情（含床位列表）")
    public Result<DormRoom> detail(@PathVariable Long id) {
        return Result.ok(roomService.getDetail(id));
    }

    @PostMapping("/batch")
    @Operation(summary = "批量创建房间")
    public Result<Void> batchCreate(@Valid @RequestBody RoomBatchCreateDTO dto) {
        roomService.batchCreate(dto);
        return Result.ok();
    }

    @PutMapping("/{id}/beds")
    @Operation(summary = "更新房间床位")
    public Result<Void> updateBeds(@PathVariable Long id, @RequestBody String bedConfig) {
        roomService.updateBeds(id, bedConfig);
        return Result.ok();
    }

    @GetMapping("/export")
    @Operation(summary = "导出房间数据")
    public Result<Void> exportData(
            @RequestParam(required = false) Long buildingId,
            @RequestParam(required = false) Integer floorNumber,
            @RequestParam(required = false) String status) {
        roomService.exportData(buildingId, floorNumber, status);
        return Result.ok();
    }
}
