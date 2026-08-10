package com.huashui.evaluation.lisenter;

import com.huashui.common.constants.MQConstants;
import com.huashui.common.domain.mqMessage.EvaluationEvent;
import com.huashui.evaluation.service.EvaluationQuestionnaireService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
@RequiredArgsConstructor
@Slf4j
public class EvaluationStartListener {


    private final EvaluationQuestionnaireService questionnaireService;

    private final StringRedisTemplate redisTemplate;


    //监听评价活动开始的死信队列
    @RabbitListener(queues="evaluation.start.queue")
    public void listen(EvaluationEvent event){
        //Redis校验,查询redis里的版本号
        Long currentVersion = Long.valueOf(Objects.requireNonNull(redisTemplate.opsForValue()
                .get("evaluation:version:start" + event.getQuestionnaireId())));

        //旧消息直接丢弃
        if(!Objects.equals(currentVersion,event.getVersion())){
            return;
        }
        questionnaireService.start(event);
    }

}