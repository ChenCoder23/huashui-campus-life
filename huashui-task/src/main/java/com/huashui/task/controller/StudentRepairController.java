package com.huashui.task.controller;

import com.huashui.api.domain.vo.dorm.room.RoomDetailVO;
import com.huashui.common.response.PageResult;
import com.huashui.common.response.Result;
import com.huashui.task.domain.dto.query.RepairQueryDTO;
import com.huashui.task.domain.dto.repair.RepairCreateDTO;
import com.huashui.task.domain.vo.repair.RepairVO;
import com.huashui.task.domain.vo.repair.repairDetailVO;
import com.huashui.task.service.RepairOrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * @author
 */

@RestController
@RequestMapping("/repair/student")
@RequiredArgsConstructor
@Tag(name = "学生报修管理")
public class StudentRepairController {

    private final RepairOrderService repairService;


    @PostMapping
    @Operation(summary = "提交报修")
    public Result<Void> create(@RequestBody RepairCreateDTO dto){
        repairService.createRepairOrder(dto);
        return Result.ok();
    }

    @GetMapping("/page")
    @Operation(summary = "我的报修列表")
    public PageResult<RepairVO> page(RepairQueryDTO dto){
        return repairService.getMyRepairPage(dto);
    }

    @GetMapping("/{id}")
    @Operation(summary = "报修详情")
    public Result<repairDetailVO> detail(@PathVariable Long id){
        return repairService.getDetailByOrderId(id);
    }

    @PutMapping("/cancel/{id}")
    @Operation(summary = "取消报修")
    public Result<Void> cancel(@PathVariable Long id){
        repairService.cancelOrder(id);
        return Result.ok();
    }
}
