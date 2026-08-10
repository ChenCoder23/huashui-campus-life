package com.huashui.common.constants;

/**
 * RabbitMQ 交换机 / 队列 / 路由键常量
 */
public final class MQConstants {

    private MQConstants() {}

    // ---- 交换机 ----
    public static final String TOPIC_EXCHANGE = "huashui.topic";



    // ---- 报修 ----
    public static final String REPAIR_QUEUE = "huashui.repair.queue";
    public static final String REPAIR_CREATED_KEY = "repair.created";
    public static final String REPAIR_ASSIGNED_KEY = "repair.assigned";
    public static final String REPAIR_COMPLETED_MESSAGE_KEY = "repair.completed.message";
    public static final String REPAIR_COMPLETED_EVALUATION_KEY = "repair.completed.evaluation";
    public static final String REPAIR_CANCEL_KEY = "repair.cancel";

    // ---- 请假 ----
    public static final String LEAVE_QUEUE = "huashui.leave.queue";
    public static final String LEAVE_SUBMITTED_KEY = "leave.submitted";
    public static final String LEAVE_APPROVED_KEY = "leave.approved";
    public static final String LEAVE_REJECTED_KEY = "leave.rejected";

    // ---- 缴费 ----
    public static final String PAY_QUEUE = "huashui.pay.queue";
    public static final String PAY_COMPLETED_KEY = "payment.completed";

    // ---- 低余额告警 ----
    public static final String ALERT_QUEUE = "huashui.alert.queue";
    public static final String LOW_BALANCE_KEY = "utility.low_balance";


    // ---- 学生信息更新 ----
    public static final String UPDATE_LOGIN_KEY = "user.login";
    public static final String UPDATE_LOGIN_QUEUE = "updateUserLoginTime";

    // -----评估-----
    //延迟交换机
    public static final String DELAY_EXCHANGE = "evaluation.delay.exchange";
    //死信交换机
    public static final String DLX_EXCHANGE = "evaluation.dlx.exchange";
    //评估问卷开始延迟队列
    public static final String EVALUATION_START_DELAY_QUEUE = "evaluation.start.delay.queue";
    //评估问卷结束延迟队列
    public static final String EVALUATION_END_DELAY_QUEUE = "evaluation.finish.delay.queue";

    //评估问卷开始死信业务队列
    public static final String EVALUATION_START_QUEUE = "evaluation.start.queue";
    //评估问卷结束死信业务队列队列
    public static final String EVALUATION_END_QUEUE = "evaluation.finish.queue";
}
