package com.huashui.auth.listener;

import com.huashui.auth.service.SysUserService;
import com.huashui.common.constants.MQConstants;
import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author
 */
public class userListener {

    @Autowired
    private SysUserService userService;


    //根据用户id更新新登录时间
    @RabbitListener(
            bindings = @QueueBinding(
                    value = @Queue(name =MQConstants.UPDATE_LOGIN_QUEUE,durable = "true"), // 队列
                    exchange =  @Exchange(name = MQConstants.TOPIC_EXCHANGE,type = ExchangeTypes.TOPIC),//交换机
                    key = MQConstants.UPDATE_LOGIN_KEY // key
            )
    )
    public void updateLoginTime(Long userId){
        userService.updateLoginTime(userId);
    }
}
