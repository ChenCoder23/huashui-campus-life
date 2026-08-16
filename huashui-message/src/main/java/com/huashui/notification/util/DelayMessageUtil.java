package com.huashui.notification.util;

import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.LocalDateTime;

/**
 * RabbitMQ 延迟消息工具类
 */
@Component
@RequiredArgsConstructor
public class DelayMessageUtil {

    private final RabbitTemplate rabbitTemplate;

    public void sendDelayMessage(
            String exchange,
            String routingKey,
            Object event,
            LocalDateTime executeTime) {

        long delay = Math.max(
                Duration.between(LocalDateTime.now(), executeTime).toMillis(),
                0
        );

        rabbitTemplate.convertAndSend(
                exchange,
                routingKey,
                event,
                message -> {
                    message.getMessageProperties().setExpiration(String.valueOf(delay));
                    return message;
                }
        );
    }
}
