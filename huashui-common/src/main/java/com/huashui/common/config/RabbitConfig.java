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

    /**
     * 延迟交换机
     */
    @Bean
    public DirectExchange evaluationDelayExchange(){
        return new DirectExchange(MQConstants.DELAY_EXCHANGE);
    }



    /**
     * 死信交换机
     */
    @Bean
    public DirectExchange evaluationDlxExchange(){

        return new DirectExchange(MQConstants.DLX_EXCHANGE);
    }

    //开始评价延迟队列
    @Bean
    public Queue evaluationStartDelayQueue(){
        return QueueBuilder
                .durable(MQConstants.EVALUATION_START_DELAY_QUEUE)
                //死信交换机
                .deadLetterExchange(MQConstants.DLX_EXCHANGE)
                //死信routingKey
                .deadLetterRoutingKey("evaluation.start")
                .build();

    }

    //结束评价延迟队列
    @Bean
    public Queue evaluationFinishDelayQueue(){

        return QueueBuilder
                .durable("evaluation.finish.delay.queue")
                //死信交换机
                .deadLetterExchange(MQConstants.DLX_EXCHANGE)
                //死信routingKey
                .deadLetterRoutingKey("evaluation.finish")
                .build();

    }

    //绑定延迟交换机
    @Bean
    public Binding startDelayBinding(){

        return BindingBuilder
                //指定绑定队列
                .bind(evaluationStartDelayQueue())
                //指定绑定交换机
                .to(evaluationDelayExchange())
                .with("evaluation.start.delay");

    }

    @Bean
    public Binding finishDelayBinding(){

        return BindingBuilder
                //指定绑定队列
                .bind(evaluationFinishDelayQueue())
                //指定绑定交换机
                .to(evaluationDelayExchange())
                .with("evaluation.finish.delay");

    }

    //绑定死信交换机
    @Bean
    public Binding startBinding(){
        return BindingBuilder
                .bind(new Queue("evaluation.start.queue"))
                .to(evaluationDlxExchange())
                //指定监听时的routeKey
                .with("evaluation.start");
    }


    @Bean
    public Binding finishBinding(){

        return BindingBuilder
                .bind(new Queue("evaluation.finish.queue"))
                .to(evaluationDlxExchange())
                //指定监听时的routeKey
                .with("evaluation.finish");

    }

}
