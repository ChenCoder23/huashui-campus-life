package com.huashui.task.controller;

import com.huashui.common.response.PageResult;
import com.huashui.common.response.Result;
import com.huashui.task.domain.dto.template.TaskTemplateDTO;
import com.huashui.task.domain.dto.query.TaskTemplateQueryDTO;
import com.huashui.task.domain.vo.template.CleanTaskTemplateDetailVO;
import com.huashui.task.domain.vo.template.CleanTaskTemplateVO;
import com.huashui.task.service.TaskTemplateService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/task/template")
@RequiredArgsConstructor
@Tag(name = "保洁任务模板管理")
public class CleanTaskTemplateController {


    private final TaskTemplateService taskTemplateService;


    /**
     * 分页查询模板
     */
    @GetMapping("/page")
    @Operation(summary = "分页查询任务模板")
    public PageResult<CleanTaskTemplateVO>page(TaskTemplateQueryDTO dto){
        return taskTemplateService.getPage(dto);
    }



    /**
     * 新增模板
     */
    @PostMapping
    @Operation(summary = "新增任务模板")
    public Result<Void> add(@RequestBody TaskTemplateDTO dto){
        taskTemplateService.add(dto);
        return Result.ok();
    }



    /**
     * 修改模板
     */
    @PutMapping
    @Operation(summary = "修改任务模板")
    public Result<Void> update(@RequestBody TaskTemplateDTO dto){
        taskTemplateService.updateTaskPlateDto(dto);
        return Result.ok();
    }



    /**
     * 删除模板
     */
    @DeleteMapping("/{id}")
    @Operation(summary = "删除任务模板")
    public Result<Void> delete(@PathVariable Long id){
        taskTemplateService.deleteTemplate(id);
        return Result.ok();
    }



    /**
     * 获取模板详情
     */
    @GetMapping("/{id}")
    @Operation(summary = "查询模板详情")
    public Result<CleanTaskTemplateDetailVO> detail(@PathVariable Long id){
        return Result.ok(taskTemplateService.detail(id));
    }

}