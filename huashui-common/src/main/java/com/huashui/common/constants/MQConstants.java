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
    public static final String LEAVE_CANCELLED_KEY = "leave.cancelled";

    // ---- 缴费 ----
    public static final String PAY_QUEUE = "huashui.pay.queue";
    public static final String PAY_COMPLETED_KEY = "payment.completed";

    // ---- 低余额告警 ----
    public static final String ALERT_QUEUE = "huashui.alert.queue";
    public static final String LOW_BALANCE_KEY = "utility.low_balance";

    // ---- 学生信息更新 ----
    public static final String UPDATE_LOGIN_KEY = "user.login";
    public static final String UPDATE_LOGIN_QUEUE = "updateUserLoginTime";

    // ----- 评价问卷 -----
    // 延迟交换机（rabbitmq_delayed_message_exchange 插件）
    public static final String DELAY_EXCHANGE = "evaluation.delay.exchange";
    // 问卷开始延迟路由键
    public static final String EVALUATION_START_DELAY_KEY = "evaluation.start.delay";
    // 问卷结束延迟路由键
    public static final String EVALUATION_FINISH_DELAY_KEY = "evaluation.end.delay";
    // 问卷开始业务队列
    public static final String EVALUATION_START_QUEUE = "evaluation.start.queue";
    // 问卷结束业务队列
    public static final String EVALUATION_END_QUEUE = "evaluation.finish.queue";

    // ----- 校园公告 -----
    // 延迟交换机（rabbitmq_delayed_message_exchange 插件）
    public static final String DELAY_EXCHANGE_NOTICE = "notice.delay.exchange";
    // 公告定时发布路由键
    public static final String NOTICE_DELAY_KEY = "notice.delay.key";
    // 公告定时发布业务队列
    public static final String NOTICE_PUBLISH_QUEUE = "notice.publish.queue";

}