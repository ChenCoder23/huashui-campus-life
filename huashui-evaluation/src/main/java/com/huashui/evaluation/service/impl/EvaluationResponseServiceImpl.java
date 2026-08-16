package com.huashui.evaluation.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.huashui.common.exception.BusinessException;
import com.huashui.common.utils.UserContext;
import com.huashui.evaluation.Enums.QuestionStatus;
import com.huashui.evaluation.Enums.QuestionType;
import com.huashui.evaluation.Enums.StudentEvaluationStatus;
import com.huashui.evaluation.domain.dto.SubmitEvaluationDTO;
import com.huashui.evaluation.domain.pojo.EvaluationAnswer;
import com.huashui.evaluation.domain.pojo.EvaluationQuestionItem;
import com.huashui.evaluation.domain.pojo.EvaluationQuestionnaire;
import com.huashui.evaluation.domain.pojo.EvaluationResponse;
import com.huashui.evaluation.domain.vo.QuestionItemVO;
import com.huashui.evaluation.domain.vo.QuestionnaireDetailVO;
import com.huashui.evaluation.mapper.EvaluationResponseMapper;
import com.huashui.evaluation.service.EvaluationAnswerService;
import com.huashui.evaluation.service.EvaluationQuestionItemService;
import com.huashui.evaluation.service.EvaluationQuestionnaireService;
import com.huashui.evaluation.service.EvaluationResponseService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author
 */
@Service
@RequiredArgsConstructor
public class EvaluationResponseServiceImpl extends ServiceImpl<EvaluationResponseMapper, EvaluationResponse> implements EvaluationResponseService {

    private final EvaluationQuestionnaireService questionnaireService;

    private final RedisTemplate<String ,Long> redisTemplate;

    private final EvaluationQuestionItemService questionItemService;

    private final EvaluationAnswerService answerService;

    @Override
    public void submit(Long questionnaireId, SubmitEvaluationDTO dto) {

        //获取当前学生的id
        Long studentId = UserContext.getUserId();

        //1. 查询问卷
        EvaluationQuestionnaire questionnaire = questionnaireService.getById(questionnaireId);

        if (questionnaire == null) {
            throw new BusinessException("评价问卷不存在");
        }

        //2. 校验问卷状态
        if (questionnaire.getStatus() != QuestionStatus.RUNNING) {
            throw new BusinessException("当前问卷不可评价");
        }

        //3. Redis校验学生是否需要评价
        String key = "evaluation:waiting:" + questionnaireId;
        Boolean member = redisTemplate.opsForSet()
                .isMember(key, studentId);
        if (!Boolean.TRUE.equals(member)) {
            throw new BusinessException("该评价已完成或不存在");

        }

        //4. 校验答案
        if (CollUtil.isEmpty(dto.getAnswers())) {
            throw new BusinessException("评价内容不能为空");
        }

        //5. 查询问题
        List<EvaluationQuestionItem> questions =
                questionItemService.list(new LambdaQueryWrapper<EvaluationQuestionItem>()
                        .eq(EvaluationQuestionItem::getQuestionnaireId, questionnaireId));

        //整理为Map
        Map<Long, EvaluationQuestionItem> questionMap = questions.stream().collect(Collectors.toMap(
                EvaluationQuestionItem::getId,
                Function.identity()));

        //6. 创建评价记录
        EvaluationResponse response = new EvaluationResponse();
        //包装属性
        response.setQuestionnaireId(questionnaireId);
        response.setStudentId(studentId);
        response.setStatus(StudentEvaluationStatus.SUBMITTED);
        response.setSubmitTime(LocalDateTime.now());
        save(response);

        //7. 保存答案
        List<EvaluationAnswer> answers =
                dto.getAnswers()
                        .stream()
                        .map(item -> {
                            EvaluationQuestionItem question = questionMap.get(item.getQuestionId());
                            if (question == null) {
                                throw new BusinessException("存在非法问题");
                            }

                            checkAnswer(question, item);
                            EvaluationAnswer answer = new EvaluationAnswer();
                            answer.setResponseId(response.getId());
                            BeanUtil.copyProperties(item, answer);
                            return answer;
                        })
                        .toList();
        //批量保存
        answerService.saveBatch(answers);
        //8. Redis移除学生
        redisTemplate.opsForSet().remove(key, studentId);

    }

    @Override
    public QuestionnaireDetailVO detail(Long questionnaireId) {

        //获取当前学生的id
        Long studentId = UserContext.getUserId();

        //2. 查询问卷
        EvaluationQuestionnaire questionnaire =questionnaireService.getById(questionnaireId);

        if(questionnaire == null){
            throw new BusinessException("问卷不存在");
        }

        //3. 查询问题
        List<EvaluationQuestionItem> questions = questionItemService.list(
                        new LambdaQueryWrapper<EvaluationQuestionItem>()
                                .eq(EvaluationQuestionItem::getQuestionnaireId, questionnaireId)
                                .orderByAsc(EvaluationQuestionItem::getSort));

        //4. 封装VO
        QuestionnaireDetailVO vo = new QuestionnaireDetailVO();
        BeanUtil.copyProperties(questionnaire, vo);
        BeanUtil.copyProperties(questionnaire, vo);
        List<QuestionItemVO> questionItemVOS = BeanUtil.copyToList(questions, QuestionItemVO.class);
        vo.setQuestions(questionItemVOS);
        return vo;

    }

    /**
     * 校验答案
     *
     * @param question 问题定义
     * @param answerDTO 学生提交答案
     */
    private void checkAnswer(EvaluationQuestionItem question, SubmitEvaluationDTO.AnswerDTO answerDTO) {

        //评分类型
        if(question.getType() == QuestionType.SCORE){
            //评分不能为空
            if(answerDTO.getScore() == null){
                throw new BusinessException("评分问题不能为空");
            }
            //评分范围校验
            if(answerDTO.getScore() < question.getMinScore()
                    || answerDTO.getScore() > question.getMaxScore()){
                throw new BusinessException(
                        "评分必须在"
                                + question.getMinScore()
                                + "-"
                                + question.getMaxScore()
                                + "之间");
            }


        }

        //文字类型
        if(question.getType() == QuestionType.TEXT){
            //必填文字不能为空
            if(question.getRequiredFlag().getValue() == 1
                    &&
                    StrUtil.isBlank(answerDTO.getContent())){
                throw new BusinessException("文字回答不能为空");
            }
        }

    }

}
