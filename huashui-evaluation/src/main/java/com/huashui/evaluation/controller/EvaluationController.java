package com.huashui.evaluation.controller;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.huashui.common.response.Result;
import com.huashui.evaluation.domain.pojo.*;
import com.huashui.evaluation.service.EvaluationService;
import io.swagger.v3.oas.annotations.Operation; import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController @RequestMapping("/evaluation") @RequiredArgsConstructor @Tag(name = "评价管理")
public class EvaluationController {
    private final EvaluationService evaluationService;
    @GetMapping("/config") @Operation(summary = "评价活动列表")
    public Result<Page<EvaluationConfig>> configList(@RequestParam(defaultValue = "1") Integer page, @RequestParam(defaultValue = "10") Integer size) {
        return Result.ok(evaluationService.configPage(page, size));
    }
    @GetMapping("/result") @Operation(summary = "评价结果列表")
    public Result<Page<EvaluationResult>> resultList(@RequestParam(defaultValue = "1") Integer page, @RequestParam(defaultValue = "10") Integer size, @RequestParam(required = false) Long configId) {
        return Result.ok(evaluationService.resultPage(page, size, configId));
    }
}