package com.huashui.attendance.listener;

import com.huashui.api.domain.dto.attendance.LeaveAttendanceDTO;
import com.huashui.attendance.service.AttendanceRecordService;
import com.huashui.common.constants.MQConstants;
import com.huashui.common.domain.mqMessage.LeaveEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/**
 * 保洁请假审批通过后，联动写入/更新考勤记录
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class LeaveApprovedListener {

    private final AttendanceRecordService attendanceService;

    /**
     * 监听请假审批通过事件
     */
    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(value = MQConstants.LEAVE_QUEUE, durable = "true"),
            exchange = @Exchange(
                    value = MQConstants.TOPIC_EXCHANGE,
                    type = ExchangeTypes.TOPIC),
            key = MQConstants.LEAVE_APPROVED_KEY))
    public void onLeaveApproved(LeaveEvent event) {
        if (event == null || event.getApplicantId() == null) {
            return;
        }
        // 只处理保洁人员的请假
        if (event.getApplicantType() == null
                || !event.getApplicantType().contains("CLEANER")) {
            return;
        }
        if (event.getStartTime() == null || event.getEndTime() == null) {
            log.warn("请假审批事件缺少起止时间，忽略, leaveId={}", event.getLeaveId());
            return;
        }

        LeaveAttendanceDTO dto = LeaveAttendanceDTO.builder()
                .workerId(event.getApplicantId())
                .workerName(event.getApplicantName())
                .campusId(event.getCampusId())
                .startDate(event.getStartTime().toLocalDate())
                .endDate(event.getEndTime().toLocalDate())
                .remark("请假：" + event.getLeaveType())
                .build();

        attendanceService.addLeaveRecord(dto);
    }
}
