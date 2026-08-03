package com.huashui.auth.util.MQ;


import com.huashui.common.constants.MQConstants;
import com.huashui.common.domain.dto.UserSimpleInfo;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author
 */
@Component
public class mqUtil {


    @Autowired
    private  RabbitTemplate mqTemplate;

    public  void updateUserInfo(UserSimpleInfo userInfo){
        mqTemplate.convertAndSend(
                MQConstants.TOPIC_EXCHANGE,
                MQConstants.UPDATE_LOGIN_KEY,
                userInfo
        );
    }
}
