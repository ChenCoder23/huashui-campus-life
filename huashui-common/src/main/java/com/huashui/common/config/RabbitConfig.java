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

        //  自动 ACK 模式（正常返回就 ack，抛异常就 nack）
        factory.setAcknowledgeMode(AcknowledgeMode.AUTO);

        //  Prefetch：一次拿多少条
        factory.setPrefetchCount(10);

        //  并发消费者数量
        factory.setConcurrentConsumers(2);  // 初始 2 个
        factory.setMaxConcurrentConsumers(5); // 最多 5 个
        return factory;
    }

    // =========================================================
    // 延迟交换机（rabbitmq_delayed_message_exchange 插件）
    //
    // 说明：使用 x-delayed-message 类型交换机，按每条消息的 x-delay
    // 头独立延迟，消息之间不会因为“先入队的旧消息延迟更长”而互相阻塞，
    // 因此不再需要“延迟队列 + 死信队列”的 TTL 方案。
    // =========================================================

    /**
     * 评价模块延迟交换机
     */
    @Bean
    public CustomExchange evaluationDelayExchange() {
        return buildDelayedExchange(MQConstants.DELAY_EXCHANGE);
    }

    /**
     * 公告模块延迟交换机
     */
    @Bean
    public CustomExchange noticeDelayExchange() {
        return buildDelayedExchange(MQConstants.DELAY_EXCHANGE_NOTICE);
    }

    private CustomExchange buildDelayedExchange(String name) {
        Map<String, Object> args = new HashMap<>();
        // 延迟交换机底层按 direct 方式路由
        args.put("x-delayed-type", "direct");
        return new CustomExchange(name, "x-delayed-message", true, false, args);
    }

    // =========================================================
    // 业务消费队列（直接绑定到延迟交换机）
    // =========================================================

    /**
     * 评价开始消费队列
     */
    @Bean
    public Queue evaluationStartQueue() {
        return QueueBuilder
                .durable(MQConstants.EVALUATION_START_QUEUE)
                .build();
    }

    /**
     * 评价结束消费队列
     */
    @Bean
    public Queue evaluationFinishQueue() {
        return QueueBuilder
                .durable(MQConstants.EVALUATION_END_QUEUE)
                .build();
    }

    /**
     * 公告定时发布消费队列
     */
    @Bean
    public Queue noticePublishQueue() {
        return QueueBuilder
                .durable(MQConstants.NOTICE_PUBLISH_QUEUE)
                .build();
    }

    // =========================================================
    // Binding：业务队列直接绑定到延迟交换机
    // =========================================================

    @Bean
    public Binding evaluationStartBinding() {
        return BindingBuilder
                .bind(evaluationStartQueue())
                .to(evaluationDelayExchange())
                .with(MQConstants.EVALUATION_START_DELAY_KEY)
                .noargs();
    }

    @Bean
    public Binding evaluationFinishBinding() {
        return BindingBuilder
                .bind(evaluationFinishQueue())
                .to(evaluationDelayExchange())
                .with(MQConstants.EVALUATION_FINISH_DELAY_KEY)
                .noargs();
    }

    @Bean
    public Binding noticePublishBinding() {
        return BindingBuilder
                .bind(noticePublishQueue())
                .to(noticeDelayExchange())
                .with(MQConstants.NOTICE_DELAY_KEY)
                .noargs();
    }
}