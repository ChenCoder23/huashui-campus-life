package com.huashui.evaluation.controller;


import com.huashui.common.response.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 评价统计管理
 */
@RestController
@RequestMapping("/evaluation/statistics")
@RequiredArgsConstructor
@Tag(name = "评价统计管理")
public class EvaluationResultController {

    private final EvaluationStatisticsService statisticsService;



    /**
     * 查看问卷统计
     */
    @GetMapping("/{questionnaireId}")
    @Operation(summary = "查看问卷统计")
    public Result<QuestionnaireStatisticsVO> detail(@PathVariable Long questionnaireId){

        return Result.ok(statisticsService.statistics(questionnaireId));

    }



    /**
     * 查看问题统计详情
     *
     * 包含:
     * 1. 平均分
     * 2. 分布情况
     * 3. 文字建议
     */
    @GetMapping("/{questionnaireId}/questions")
    @Operation(summary = "查看问题统计")
    public Result<List<QuestionStatisticsVO>> questionStatistics(
            @PathVariable Long questionnaireId){

        return Result.ok(
                statisticsService.questionStatistics(questionnaireId)
        );

    }


}
