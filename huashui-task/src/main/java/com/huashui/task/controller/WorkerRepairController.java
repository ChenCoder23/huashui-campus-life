package com.huashui.task.controller;



import com.huashui.common.response.PageResult;
import com.huashui.common.response.Result;
import com.huashui.task.domain.dto.query.RepairQueryDTO;
import com.huashui.task.domain.dto.repair.RepairCompleteDTO;
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
@RequestMapping("/repair/worker")
@RequiredArgsConstructor
public class WorkerRepairController {

    private final RepairOrderService repairService;

    @GetMapping("/page")
    @Operation(summary = "我的维修任务")
    public PageResult<RepairVO> page(RepairQueryDTO dto){

        return repairService.workerRepairPage(dto);
    }

    @GetMapping("/{id}")
    @Operation(summary = "维修详情")
    public Result<repairDetailVO> detail(@PathVariable Long id){
        return repairService.getDetailByOrderId(id);
    }

    @PutMapping("/start/{id}")
    @Operation(summary = "开始维修")
    public Result<Void> start(@PathVariable Long id){
        repairService.start(id);
        return Result.ok();
    }

    @PutMapping("/complete")
    @Operation(summary = "完成维修")
    public Result<Void> complete(@RequestBody RepairCompleteDTO dto){

        repairService.complete(dto);

        return Result.ok();
    }


}
