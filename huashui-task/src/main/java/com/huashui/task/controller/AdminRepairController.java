package com.huashui.task.controller;

import com.huashui.common.response.PageResult;
import com.huashui.common.response.Result;
import com.huashui.task.domain.dto.query.RepairQueryDTO;
import com.huashui.task.domain.dto.repair.RepairAssignDTO;
import com.huashui.task.domain.vo.repair.RepairVO;
import com.huashui.task.domain.vo.repair.repairDetailVO;
import com.huashui.task.service.RepairOrderService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * @author
 */

@RestController
@RequestMapping("/repair/admin")
@RequiredArgsConstructor
public class AdminRepairController {

    private final RepairOrderService repairService;

    @GetMapping("/page")
    @Operation(summary = "宿舍管理员查询报修工单")
    public Result<PageResult<RepairVO>> page(RepairQueryDTO dto) {
        return Result.ok(repairService.adminPage(dto));
    }




    @GetMapping("/{id}")
    @Operation(summary = "报修详情")
    public Result<repairDetailVO> detail(@PathVariable Long id){
        return repairService.getDetailByOrderId(id);
    }


    @PutMapping("/assign")
    @Operation(summary="派单")
    public Result<Void> assign(@RequestBody RepairAssignDTO dto){

        repairService.assign(dto);

        return Result.ok();
    }
}
