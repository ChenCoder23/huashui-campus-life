package com.huashui.evaluation.controller;

import com.huashui.common.response.PageResult;
import com.huashui.common.response.Result;
import com.huashui.evaluation.domain.dto.CreateQuestionnaireDTO;
import com.huashui.evaluation.domain.dto.QuestionnaireQueryDTO;
import com.huashui.evaluation.domain.dto.UpdateQuestionnaireDTO;
import com.huashui.evaluation.domain.vo.QuestionnaireDetailVO;
import com.huashui.evaluation.domain.vo.QuestionnaireVO;
import com.huashui.evaluation.service.EvaluationQuestionnaireService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;


/**
 * 评价问卷管理
 *
 * 管理员/宿管使用
 */
@RestController
@RequestMapping("/evaluation/questionnaire")
@RequiredArgsConstructor
@Tag(name = "评价问卷管理", description = "管理员创建、管理评价问卷")
public class EvaluationAdminController {


    private final EvaluationQuestionnaireService questionnaireService;



    /**
     * 创建评价问卷
     *
     * 创建内容:
     * 1. 保存问卷
     * 2. 保存问题
     * 3. 根据范围生成评价对象
     * 4. 发送延迟消息
     */
    @PostMapping
    @Operation(summary = "创建评价问卷")
    public Result<Long> create(@RequestBody CreateQuestionnaireDTO dto){
        Long id = questionnaireService.create(dto);
        return Result.ok(id);
    }



    /**
     * 查询自己创建的评价问卷
     */
    @GetMapping("/page")
    @Operation(summary = "分页查询我的评价问卷")
    public PageResult<QuestionnaireVO> page(QuestionnaireQueryDTO dto){
        return questionnaireService.getEvaluationPage(dto);
    }



    /**
     * 查看问卷详情
     *
     * 包含:
     * 1. 问卷基本信息
     * 2. 评价问题列表
     */
    @GetMapping("/{id}")
    @Operation(summary = "评价问卷详情")
    public Result<QuestionnaireDetailVO> detail(@PathVariable Long id){
        return Result.ok(questionnaireService.detail(id));
    }



    /**
     * 修改问卷
     *
     * 只有WAITING状态允许修改
     */
    @PutMapping("/{id}")
    @Operation(summary = "修改评价问卷")
    public Result<Void> update(@PathVariable Long id, @RequestBody UpdateQuestionnaireDTO dto){
        questionnaireService.updateEvaluation(id,dto);
        return Result.ok();
    }




    /**
     * 删除问卷
     *
     * 只有未开始问卷允许删除
     */
    @DeleteMapping("/{id}")
    @Operation(summary = "删除评价问卷")
    public Result<Void> delete(@PathVariable Long id){
        questionnaireService.deleteEvaluation(id);
        return Result.ok();
    }





    /**
     * 提前结束评价
     *
     * RUNNING状态可以提前结束
     */
    @PutMapping("/{id}/finish")
    @Operation(summary = "提前结束评价问卷")
    public Result<Void> finish(@PathVariable Long id){
        questionnaireService.PreFinish(id);
        return Result.ok();
    }

}