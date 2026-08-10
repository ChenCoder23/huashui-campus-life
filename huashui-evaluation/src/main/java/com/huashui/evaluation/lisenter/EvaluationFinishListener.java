package com.huashui.evaluation.lisenter;

import com.huashui.common.constants.MQConstants;
import com.huashui.common.domain.mqMessage.EvaluationEvent;
import com.huashui.evaluation.service.EvaluationQuestionnaireService;
import lombok.RequiredArgsConstructor;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
@RequiredArgsConstructor
public class EvaluationFinishListener {



    private final EvaluationQuestionnaireService service;
    private final StringRedisTemplate redisTemplate;



    //监听评价活动结束的死信队列
    @RabbitListener(queues= MQConstants.EVALUATION_END_QUEUE)
    public void listen(EvaluationEvent event){
        //Redis校验,查询redis里的版本号
        Long currentVersion = Long.valueOf(Objects.requireNonNull(redisTemplate.opsForValue()
                .get("evaluation:version:start" + event.getQuestionnaireId())));
        //旧消息直接丢弃
        if(!Objects.equals(currentVersion,event.getVersion())){
            return;
        }
        service.finish(event);
    }

}