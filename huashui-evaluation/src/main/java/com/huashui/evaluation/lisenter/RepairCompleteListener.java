package com.huashui.evaluation.lisenter;

import com.huashui.common.constants.MQConstants;
import com.huashui.common.domain.mqMessage.RepairEvent;
import com.huashui.evaluation.service.EvaluationRecordService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class RepairCompleteListener {


    private final EvaluationRecordService evaluationService;


    @RabbitListener(
            bindings = @QueueBinding(
                    value = @Queue(name = MQConstants.REPAIR_QUEUE),
                    exchange = @Exchange(name = MQConstants.TOPIC_EXCHANGE, type = ExchangeTypes.TOPIC),
                    key = MQConstants.REPAIR_COMPLETED_EVALUATION_KEY))
    public void handleRepairComplete(RepairEvent event){

        log.info("收到维修完成评价消息: {}", event);
        evaluationService.createRepairEvaluation(event);

    }

}