package com.huashui.evaluation.lisenter;

import com.huashui.common.constants.MQConstants;
import com.huashui.common.domain.mqMessage.EvaluationEvent;
import com.huashui.evaluation.service.EvaluationQuestionnaireService;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class EvaluationFinishListener {



    private final EvaluationQuestionnaireService service;



    //监听评价活动结束的死信队列
    @RabbitListener(queues = MQConstants.EVALUATION_END_QUEUE)
    public void listen(EvaluationEvent event){
        //校验逻辑下沉到 service：执行前对比数据库状态与截止时间
        service.finish(event);
    }

}
