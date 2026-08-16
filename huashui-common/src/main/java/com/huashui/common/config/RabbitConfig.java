package com.huashui.common.config;

import com.huashui.common.constants.MQConstants;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.listener.RabbitListenerContainerFactory;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

/**
 * Spring AMQP 配置类
 *
 */
@Configuration
public class RabbitConfig {

    // ========== RabbitMQ 连接 ==========

    @Bean
    public ConnectionFactory connectionFactory() {
        CachingConnectionFactory factory = new CachingConnectionFactory();
        factory.setHost("172.25.118.113");
        factory.setPort(5672);
        factory.setUsername("huashuiNB666");
        factory.setPassword("huashuiNB666pw");  // 改成你的密码
        factory.setVirtualHost("/");
        // 开启 Publisher Confirm
        factory.setPublisherConfirmType(CachingConnectionFactory.ConfirmType.CORRELATED);
        // 开启 Publisher Returns（消息无法路由时回调）
        factory.setPublisherReturns(true);
        return factory;
    }



    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate template = new RabbitTemplate(connectionFactory);
        // JSON 序列化
        template.setMessageConverter(jacksonConverter());

        //  消息无法路由到队列时的回调
        template.setReturnsCallback(returned -> {
            System.out.println("   消息未路由 exchange=" +
                    returned.getExchange() + " routingKey=" + returned.getRoutingKey() +
                    " replyText=" + returned.getReplyText());
        });

        //  Confirm 回调
        template.setConfirmCallback((correlationData, ack, cause) -> {
            if (ack) {
                System.out.println("  消息确认, id=" +
                        (correlationData != null ? correlationData.getId() : "null"));
            } else {
                System.out.println(" 消息失败cause=" + cause);
            }
        });

        return template;
    }



    @Bean
    public MessageConverter jacksonConverter() {
        Jackson2JsonMessageConverter converter = new Jackson2JsonMessageConverter();
        // ★ 自动类型推断（消费端不需要强转）
        converter.setCreateMessageIds(true);
        return converter;
    }



    @Bean
    public RabbitListenerContainerFactory<?> rabbitListenerContainerFactory(
            ConnectionFactory connectionFactory) {
        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory);

        //  消费端也设置 JSON 转换
        factory.setMessageConverter(jacksonConverter());

        //  手动 ACK 模式（默认 AUTO：正常返回就 ack，抛异常就 nack）
        factory.setAcknowledgeMode(AcknowledgeMode.MANUAL); // 改成 MANUAL 需要手动 basicAck

        //  Prefetch：一次拿多少条
        factory.setPrefetchCount(10);

        //  并发消费者数量
        factory.setConcurrentConsumers(2);  // 初始 2 个
        factory.setMaxConcurrentConsumers(5); // 最多 5 个
        return factory;
    }

    // =========================================================
    // 评价模块 Exchange
    // =========================================================

    /**
     * 评价模块延迟交换机
     */
    @Bean
    public DirectExchange evaluationDelayExchange() {

        return new DirectExchange(
                MQConstants.DELAY_EXCHANGE
        );
    }

    /**
     * 评价模块死信交换机
     */
    @Bean
    public DirectExchange evaluationDlxExchange() {

        return new DirectExchange(
                MQConstants.DLX_EXCHANGE
        );
    }


    // =========================================================
    // 公告模块 Exchange
    // =========================================================

    /**
     * 公告延迟交换机
     */
    @Bean
    public DirectExchange noticeDelayExchange() {

        return new DirectExchange(
                MQConstants.DELAY_EXCHANGE_NOTICE
        );
    }

    /**
     * 公告死信交换机
     */
    @Bean
    public DirectExchange noticeDlxExchange() {

        return new DirectExchange(
                MQConstants.DLX_EXCHANGE_NOTICE
        );
    }


    // =========================================================
    // 评价模块：延迟队列
    // =========================================================

    /**
     * 评价开始延迟队列
     */
    @Bean
    public Queue evaluationStartDelayQueue() {

        return QueueBuilder
                .durable(
                        MQConstants.EVALUATION_START_DELAY_QUEUE
                )
                .deadLetterExchange(
                        MQConstants.DLX_EXCHANGE
                )
                .deadLetterRoutingKey(
                        MQConstants.EVALUATION_START_KEY
                )
                .build();
    }


    /**
     * 评价结束延迟队列
     */
    @Bean
    public Queue evaluationFinishDelayQueue() {

        return QueueBuilder
                .durable(
                        MQConstants.EVALUATION_END_DELAY_QUEUE
                )
                .deadLetterExchange(
                        MQConstants.DLX_EXCHANGE
                )
                .deadLetterRoutingKey(
                        MQConstants.EVALUATION_FINISH_KEY
                )
                .build();
    }


    // =========================================================
    // 评价模块：最终消费队列
    // =========================================================

    /**
     * 评价开始消费队列
     */
    @Bean
    public Queue evaluationStartQueue() {

        return QueueBuilder
                .durable(
                        MQConstants.EVALUATION_START_QUEUE
                )
                .build();
    }


    /**
     * 评价结束消费队列
     */
    @Bean
    public Queue evaluationFinishQueue() {

        return QueueBuilder
                .durable(
                        MQConstants.EVALUATION_END_QUEUE
                )
                .build();
    }


    // =========================================================
    // 评价模块：延迟队列 Binding
    // =========================================================

    /**
     * 评价开始延迟队列绑定
     */
    @Bean
    public Binding evaluationStartDelayBinding() {

        return BindingBuilder
                .bind(evaluationStartDelayQueue())
                .to(evaluationDelayExchange())
                .with(MQConstants.EVALUATION_START_DELAY_KEY);
    }


    /**
     * 评价结束延迟队列绑定
     */
    @Bean
    public Binding evaluationFinishDelayBinding() {

        return BindingBuilder
                .bind(evaluationFinishDelayQueue())
                .to(evaluationDelayExchange())
                .with(MQConstants.EVALUATION_FINISH_DELAY_KEY);
    }


    // =========================================================
    // 评价模块：死信队列 Binding
    // =========================================================

    /**
     * 评价开始死信队列绑定
     */
    @Bean
    public Binding evaluationStartBinding() {

        return BindingBuilder
                .bind(evaluationStartQueue())
                .to(evaluationDlxExchange())
                .with(MQConstants.EVALUATION_START_KEY);
    }


    /**
     * 评价结束死信队列绑定
     */
    @Bean
    public Binding evaluationFinishBinding() {

        return BindingBuilder
                .bind(evaluationFinishQueue())
                .to(evaluationDlxExchange())
                .with(MQConstants.EVALUATION_FINISH_KEY);
    }


    // =========================================================
    // 公告模块：延迟队列
    // =========================================================

    /**
     * 公告延迟队列
     */
    @Bean
    public Queue noticeDelayQueue() {

        return QueueBuilder
                .durable(
                        MQConstants.DELAY_QUEUE_NOTICE
                )
                .deadLetterExchange(
                        MQConstants.DLX_EXCHANGE_NOTICE
                )
                .deadLetterRoutingKey(
                        MQConstants.NOTICE_DLX_KEY
                )
                .build();
    }


    // =========================================================
    // 公告模块：最终消费队列
    // =========================================================

    /**
     * 公告消费队列
     */
    @Bean
    public Queue noticeDlxQueue() {

        return QueueBuilder
                .durable(
                        MQConstants.DLX_QUEUE_NOTICE
                )
                .build();
    }


    // =========================================================
    // 公告模块：Binding
    // =========================================================

    /**
     * 公告延迟队列绑定
     */
    @Bean
    public Binding noticeDelayBinding() {

        return BindingBuilder
                .bind(noticeDelayQueue())
                .to(noticeDelayExchange())
                .with(MQConstants.NOTICE_DELAY_KEY);
    }


    /**
     * 公告死信队列绑定
     */
    @Bean
    public Binding noticeDlxBinding() {

        return BindingBuilder
                .bind(noticeDlxQueue())
                .to(noticeDlxExchange())
                .with(MQConstants.NOTICE_DLX_KEY);
    }
}
