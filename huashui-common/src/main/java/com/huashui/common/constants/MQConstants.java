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
    public static final String REPAIR_COMPLETED_KEY = "repair.completed";

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
}
