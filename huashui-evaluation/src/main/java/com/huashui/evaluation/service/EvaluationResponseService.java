package com.huashui.evaluation.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.huashui.evaluation.domain.dto.SubmitEvaluationDTO;
import com.huashui.evaluation.domain.pojo.EvaluationResponse;
import com.huashui.evaluation.domain.vo.QuestionnaireDetailVO;


public interface EvaluationResponseService extends IService<EvaluationResponse> {


    void submit(Long questionnaireId, SubmitEvaluationDTO dto);

    QuestionnaireDetailVO detail(Long questionnaireId);
}