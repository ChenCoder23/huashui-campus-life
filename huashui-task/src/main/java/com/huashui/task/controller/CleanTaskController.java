package com.huashui.task.controller;

import com.huashui.common.response.PageResult;
import com.huashui.common.response.Result;
import com.huashui.task.domain.dto.cleanTask.UpdateTaskStatusDTO;
import com.huashui.task.domain.dto.query.CleanTaskQueryDTO;
import com.huashui.task.domain.dto.template.TaskTemplateDTO;
import com.huashui.task.domain.pojo.Task;
import com.huashui.task.domain.vo.cleanTask.CleanTaskDetailVO;
import com.huashui.task.domain.vo.cleanTask.CleanTaskVO;
import com.huashui.task.service.TaskService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author
 */
@RestController
@RequestMapping("/task")
@RequiredArgsConstructor
public class CleanTaskController {

    private final TaskService taskService;

    @PostMapping("/generate")
    @Operation(summary = "生成保洁任务")
    public Result<Void> generate(@RequestBody TaskTemplateDTO dto){
        taskService.generate(dto);
        return Result.ok();
    }


    @GetMapping("/list")
    @Operation(summary="分页查询查询保洁任务")
    public PageResult<CleanTaskVO> list(CleanTaskQueryDTO dto){
        return taskService.getCleanTaskPage(dto);
    }



    @GetMapping("/my")
    @Operation(summary="今日我的保洁任务")
    public Result<List<CleanTaskVO>> myTask(){
        return Result.ok(taskService.getTodayCleanTask());
    }




    @GetMapping("/{id}")
    @Operation(summary="任务详情")
    public Result<CleanTaskDetailVO> detail(@PathVariable Long id){
        return Result.ok(taskService.getDetail(id));
    }


    @PutMapping("/status")
    @Operation(summary = "修改任务状态")
    public Result<Void> updateStatus(@RequestBody UpdateTaskStatusDTO dto){
        taskService.updateStatus(dto);
        return Result.ok();
    }
}
