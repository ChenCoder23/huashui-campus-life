package com.huashui.evaluation.lisenter;

import com.huashui.common.domain.mqMessage.EvaluationEvent;
import com.huashui.evaluation.service.EvaluationQuestionnaireService;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class EvaluationStartListener {


    private final EvaluationQuestionnaireService questionnaireService;


    //监听评价活动开始的死信队列
    @RabbitListener(queues = "evaluation.start.queue")
    public void listen(EvaluationEvent event){
        //校验逻辑下沉到 service：执行前对比数据库状态与开始时间
        questionnaireService.start(event);
    }

}
