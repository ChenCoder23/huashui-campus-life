package com.huashui.evaluation.job;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.huashui.evaluation.Enums.QuestionStatus;
import com.huashui.evaluation.domain.pojo.EvaluationQuestionnaire;
import com.huashui.evaluation.service.EvaluationQuestionnaireService;
import com.xxl.job.core.handler.annotation.XxlJob;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;

@Component
@RequiredArgsConstructor
public class EvaluationSyncJob {

    private final EvaluationQuestionnaireService questionnaireService;


    private final RedisTemplate<String, Object> redisTemplate;


    /**
     * 每10分钟同步一次评价提交人数
     */
    @XxlJob("evaluationSubmitCountSyncJob")
    public void syncSubmitCount() {

        // 1. 查询所有进行中的问卷
        List<EvaluationQuestionnaire> questionnaires =
                questionnaireService.list(new LambdaQueryWrapper<EvaluationQuestionnaire>()
                                .eq(EvaluationQuestionnaire::getStatus, QuestionStatus.RUNNING));

        if (CollUtil.isEmpty(questionnaires)) {
            return;
        }

        // 2. 遍历问卷
        for (EvaluationQuestionnaire questionnaire : questionnaires) {

            Long questionnaireId = questionnaire.getId();

            // 3. Redis Key
            String key = "evaluation:waiting:" + questionnaireId;

            // 4. 获取未评价人数
            Long waitingCount = redisTemplate.opsForSet().size(key);

            if (waitingCount == null) {
                continue;
            }

            // 5. 已评价人数
            int totalCount = questionnaire.getTotalCount();

            int submitCount = totalCount - waitingCount.intValue();

            // 防止异常情况下出现负数
            submitCount = Math.max(submitCount, 0);

            if (submitCount == questionnaire.getTotalCount()){
                //所有学生完成评价,自动结束本次评价活动
                questionnaire.setStatus(QuestionStatus.FINISHED);
                //回填状态
                questionnaireService.updateById(questionnaire);
            }

            // 防止超过总人数
            submitCount = Math.min(submitCount, totalCount);
            // 6. 只有数据发生变化才更新
            if (!Objects.equals(
                    questionnaire.getSubmitCount(),
                    submitCount)) {

                questionnaireService.update(
                        new LambdaUpdateWrapper<EvaluationQuestionnaire>()
                                .eq(EvaluationQuestionnaire::getId,
                                        questionnaireId)
                                .set(EvaluationQuestionnaire::getSubmitCount,
                                        submitCount));
            }
        }
    }
}