package com.huashui.evaluation.util;

import com.huashui.common.constants.MQConstants;
import com.huashui.common.domain.mqMessage.DelayedMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

/**
 * RabbitMQ 延迟消息工具类（基于 rabbitmq_delayed_message_exchange 插件）
 *
 * @author
 */
@Component
@RequiredArgsConstructor
public class DelayMessageUtil {

    // 设置默认的延迟交换机
    private static final String DEFAULT_EXCHANGE = MQConstants.DELAY_EXCHANGE;

    private final RabbitTemplate rabbitTemplate;

    /**
     * 使用默认交换机
     */
    public void sendDelayMessage(
            String routingKey,
            Object event,
            LocalDateTime executeTime) {
        sendDelayMessage(DEFAULT_EXCHANGE, routingKey, event, executeTime);
    }

    /**
     * 使用指定交换机
     */
    public void sendDelayMessage(
            String exchange,
            String routingKey,
            Object event,
            LocalDateTime executeTime) {

        // 统一到秒级，避免纳秒/毫秒精度与数据库 datetime 不一致导致等值校验失败
        LocalDateTime target = executeTime == null
                ? LocalDateTime.now()
                : executeTime.truncatedTo(ChronoUnit.SECONDS);

        // 获取延迟时间
        long delay = Math.max(Duration.between(LocalDateTime.now(), target).toMillis(), 0);

        // 消息体回填计划执行时间，消费端据此与 DB 时间做等值校验
        if (event instanceof DelayedMessage delayedMessage) {
            delayedMessage.setExecuteTime(target);
        }

        rabbitTemplate.convertAndSend(
                exchange,
                routingKey,
                event,
                message -> {
                    // 延迟消息插件：用 x-delay 头，而不是 per-message TTL（避免队头阻塞）
                    message.getMessageProperties().setHeader("x-delay", delay);
                    return message;
                }
        );
    }
}