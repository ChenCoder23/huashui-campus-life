package com.huashui.evaluation.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.huashui.common.domain.mqMessage.EvaluationEvent;
import com.huashui.common.response.PageResult;
import com.huashui.evaluation.domain.dto.CreateQuestionnaireDTO;
import com.huashui.evaluation.domain.dto.QuestionnaireQueryDTO;
import com.huashui.evaluation.domain.dto.UpdateQuestionnaireDTO;
import com.huashui.evaluation.domain.pojo.EvaluationQuestionnaire;
import com.huashui.evaluation.domain.vo.QuestionnaireDetailVO;
import com.huashui.evaluation.domain.vo.QuestionnaireVO;


public interface EvaluationQuestionnaireService extends IService<EvaluationQuestionnaire> {



    Long create(CreateQuestionnaireDTO dto);

    void start(EvaluationEvent questionnaireId);

    void finish(EvaluationEvent questionnaireId);

    PageResult<QuestionnaireVO> getEvaluationPage(QuestionnaireQueryDTO dto);

    QuestionnaireDetailVO detail(Long id);



    void updateEvaluation(Long id, UpdateQuestionnaireDTO dto);

    void deleteEvaluation(Long id);

    void PreFinish(Long id);
}