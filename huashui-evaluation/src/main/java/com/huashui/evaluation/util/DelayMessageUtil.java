package com.huashui.evaluation.util;

import com.huashui.common.constants.MQConstants;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.LocalDateTime;

/**
 * RabbitMQ 延迟消息工具类
 *
 * @author
 */
@Component
@RequiredArgsConstructor
public class DelayMessageUtil {


    //设置默认的延迟交换机
    private static final String DEFAULT_EXCHANGE = MQConstants.DELAY_EXCHANGE;

    private final RabbitTemplate rabbitTemplate;


    /**
     * 使用默认交换机
     */
    public void sendDelayMessage(
            String routingKey,
            Object event,
            LocalDateTime executeTime) {
        sendDelayMessage(
                DEFAULT_EXCHANGE,
                routingKey,
                event,
                executeTime
        );
    }


    /**
     * 使用指定交换机
     */
    public void sendDelayMessage(
            String exchange,
            String routingKey,
            Object event,
            LocalDateTime executeTime) {
        //获取延迟时间
        long delay = Math.max(Duration.between(LocalDateTime.now(), executeTime).toMillis(), 0);

        rabbitTemplate.convertAndSend(
                exchange,
                routingKey,
                event,
                message -> {
                    //设置消息的过期时间
                    message.getMessageProperties().setExpiration(String.valueOf(delay));
                    return message;
                }
        );
    }
}
