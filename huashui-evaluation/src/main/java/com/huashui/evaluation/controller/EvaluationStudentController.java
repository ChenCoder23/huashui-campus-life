package com.huashui.evaluation.controller;

import com.huashui.common.response.Result;
import com.huashui.evaluation.domain.dto.SubmitEvaluationDTO;
import com.huashui.evaluation.domain.vo.QuestionnaireDetailVO;
import com.huashui.evaluation.domain.vo.StudentQuestionnaireVO;
import com.huashui.evaluation.service.EvaluationQuestionnaireService;
import com.huashui.evaluation.service.EvaluationResponseService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;


/**
 * 学生评价接口
 ** @author 陈会闯
 *
 */
@RestController
@RequestMapping("/evaluation/response")
@RequiredArgsConstructor
@Tag(name = "学生评价管理")
public class EvaluationStudentController {

    private final EvaluationResponseService responseService;

    private final EvaluationQuestionnaireService questionnaireService;

    /**
     * 查询我的待评价问卷
     */
    @GetMapping("/list")
    @Operation(summary = "查询我的待评价问卷")
    public Result<List<StudentQuestionnaireVO>> list(){
        return Result.ok(questionnaireService.myQuestionnaire());
    }


    /**
     * 查看评价问卷详情
     *
     * 包含:
     * 1. 问卷信息
     * 2. 问题列表
     * 3. 当前填写状态,此接口有应用于答题
     */
    @GetMapping("/{questionnaireId}")
    @Operation(summary = "查看评价问卷详情")
    public Result<QuestionnaireDetailVO> detail(@PathVariable Long questionnaireId){
        return Result.ok(responseService.detail(questionnaireId));
    }


    /**
     * 提交评价
     */
    @PostMapping("/{questionnaireId}/submit")
    @Operation(summary = "提交评价")
    public Result<Void> submit(@PathVariable Long questionnaireId, @RequestBody SubmitEvaluationDTO dto){

        responseService.submit(questionnaireId, dto);

        return Result.ok();
    }
}
