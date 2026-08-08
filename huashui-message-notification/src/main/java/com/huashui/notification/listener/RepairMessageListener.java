package com.huashui.notification.listener;

import com.huashui.api.client.dorm.BuildingClient;
import com.huashui.api.client.dorm.RoomClient;
import com.huashui.api.domain.vo.dorm.room.RoomDetailVO;
import com.huashui.common.constants.MQConstants;

import com.huashui.common.domain.mqMessage.RepairEvent;
import com.huashui.notification.domain.pojo.Message;
import com.huashui.notification.service.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;

import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RepairMessageListener {


    private final MessageService messageService;

    private final BuildingClient buildingClient;

    private final RoomClient roomClient;



    @RabbitListener(bindings = @QueueBinding(
                    value = @Queue(MQConstants.REPAIR_QUEUE),
                    exchange = @Exchange(
                            value = MQConstants.TOPIC_EXCHANGE,
                            type = ExchangeTypes.TOPIC),
                    key = MQConstants.REPAIR_CREATED_KEY))
    public void handle(RepairEvent event){

        //1. 根据楼栋查询宿管
        Long managerId = buildingClient.getManagerId(event.getBuildingId());


        //2. 创建消息
        Message message = new Message();

        message.setType("REPAIR");

        message.setTitle("新的报修申请");

        message.setContent("您负责的楼栋有新的报修申请,请尽快处理");

        message.setReceiverId(managerId);


        message.setBusinessType("REPAIR_ORDER");

        message.setBusinessId(event.getRepairId());


        messageService.save(message);


    }

    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(MQConstants.REPAIR_QUEUE),
            exchange = @Exchange(
                    value = MQConstants.TOPIC_EXCHANGE,
                    type = ExchangeTypes.TOPIC),
            key = MQConstants.REPAIR_CANCEL_KEY))
    public void Cancel(RepairEvent event){

        //1. 根据楼栋查询宿管
        Long managerId = buildingClient.getManagerId(event.getBuildingId());

        //2. 创建消息
        Message message = new Message();

        message.setType("REPAIR");

        message.setTitle("报修申请被取消");

        message.setContent("刚刚有报修申请被学生撤回订单id:"+event.getRepairId()+"请注意查看");

        message.setReceiverId(managerId);

        message.setBusinessType("REPAIR_ORDER");

        message.setBusinessId(event.getRepairId());


        messageService.save(message);


    }


    /**
     * 派单
     * 通知维修工
     */
    @RabbitListener(
            bindings = @QueueBinding(
                    value = @Queue("repair.assign.queue"),
                    exchange = @Exchange(
                            value = MQConstants.TOPIC_EXCHANGE,
                            type = ExchangeTypes.TOPIC
                    ),
                    key = MQConstants.REPAIR_ASSIGNED_KEY
            )
    )
    public void assign(RepairEvent event){


        //获取房间的详细信息
        RoomDetailVO roomDetailInfo = roomClient.getRoomDetailInfoById(event.getRoomId());


        Message message = new Message();
        message.setType("REPAIR");


        message.setTitle("新的维修任务");


        message.setContent(
                "您有新的维修任务："
                        + RepairEvent.formatDescription(event.getDescription())
                        +"来自" + roomDetailInfo.getCampusName() +roomDetailInfo.getBuildingName()
                        +"房间号:"+event.getRoomId()
                +"请尽快处理"
        );


        message.setReceiverId(event.getRepairerId());


        message.setBusinessType("REPAIR");


        message.setBusinessId(event.getRepairId());


        messageService.save(message);

    }






    /**
     * 完成维修
     * 通知学生评价
     */
    @RabbitListener(
            bindings = @QueueBinding(
                    value = @Queue("repair.complete.queue"),
                    exchange = @Exchange(
                            value = MQConstants.TOPIC_EXCHANGE,
                            type = ExchangeTypes.TOPIC
                    ),
                    key = MQConstants.REPAIR_COMPLETED_MESSAGE_KEY
            )
    )
    public void complete(RepairEvent event){


        Message message = new Message();

        message.setType("EVALUATION");

        message.setTitle("维修完成");


        message.setContent("您的报修已完成，请评价本次维修服务" +
                "工单编号:"+event.getOrderNo());


        message.setReceiverId(event.getStudentId());


        message.setBusinessType("REPAIR");


        message.setBusinessId(event.getRepairId());


        messageService.save(message);

        // todo 调用评估微服务

    }
}