package com.huashui.common.domain.mqMessage;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 请假事件（MQ 消息体）
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LeaveEvent {

    /** 请假申请ID */
    private Long leaveId;

    /** 申请人ID */
    private Long applicantId;

    /** 申请人类型（STUDENT/CLEANER/REPAIRER） */
    private String applicantType;

    /** 校区ID */
    private Long campusId;

    /** 请假类型 */
    private String leaveType;

    /** 请假原因 */
    private String reason;

    /** 开始时间 */
    private LocalDateTime startTime;

    /** 结束时间 */
    private LocalDateTime endTime;

    /** 事件类型：SUBMITTED/APPROVED/REJECTED/CANCELLED */
    private String eventType;
}
