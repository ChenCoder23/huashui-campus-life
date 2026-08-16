package com.huashui.leave.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.huashui.api.client.attendance.AttendanceClient;
import com.huashui.api.domain.dto.attendance.LeaveAttendanceDTO;
import com.huashui.common.constants.MQConstants;
import com.huashui.common.domain.mqMessage.LeaveEvent;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.huashui.common.exception.BusinessException;
import com.huashui.common.utils.UserContext;
import com.huashui.leave.domain.dto.LeaveSubmitDTO;
import com.huashui.leave.domain.pojo.LeaveRequest;
import com.huashui.leave.enums.LeaveStatus;
import com.huashui.leave.mapper.LeaveRequestMapper;
import com.huashui.leave.service.LeaveRequestService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Slf4j
@Service
public class LeaveRequestServiceImpl
        extends ServiceImpl<LeaveRequestMapper, LeaveRequest>
        implements LeaveRequestService {

    private final AttendanceClient attendanceClient;

    private final RabbitTemplate rabbitTemplate;

    public LeaveRequestServiceImpl(AttendanceClient attendanceClient, RabbitTemplate rabbitTemplate) {
        this.attendanceClient = attendanceClient;
        this.rabbitTemplate = rabbitTemplate;
    }

    @Override
    public Page<LeaveRequest> page(Integer page, Integer size, String status) {
        LambdaQueryWrapper<LeaveRequest> qw = new LambdaQueryWrapper<>();
        Long userId = UserContext.getUserId();
        String roles = UserContext.getRoles();

        if (roles != null && (roles.contains("STUDENT") || roles.contains("CLEANER") || roles.contains("REPAIRER"))) {
            qw.eq(LeaveRequest::getApplicantId, userId);
        }

        if (StrUtil.isNotBlank(status)) qw.eq(LeaveRequest::getStatus, status);
        qw.orderByDesc(LeaveRequest::getCreateTime);
        return this.page(new Page<>(page, size), qw);
    }

    @Override
    @Transactional
    public void submit(LeaveSubmitDTO dto) {
        if (dto.getEndTime().isBefore(dto.getStartTime())) {
            throw new BusinessException("结束时间不能早于开始时间");
        }
        LeaveRequest req = BeanUtil.copyProperties(dto, LeaveRequest.class);
        Long applicantId = UserContext.getUserId();
        req.setApplicantId(applicantId);
        req.setApplicantType(resolveApplicantType(UserContext.getRoles()));
        req.setStatus(LeaveStatus.PENDING.getCode());
        save(req);

        // 保洁人员请假后，自动向考勤表写入"请假"状态记录
        if (isCleaner(UserContext.getRoles())) {
            LeaveAttendanceDTO attendanceDTO = LeaveAttendanceDTO.builder()
                    .workerId(applicantId)
                    .campusId(dto.getCampusId())
                    .startDate(dto.getStartTime().toLocalDate())
                    .endDate(dto.getEndTime().toLocalDate())
                    .remark("请假：" + dto.getLeaveType())
                    .build();
            attendanceClient.addLeaveRecord(attendanceDTO);
        }

        sendLeaveEvent(req, "SUBMITTED");
    }

    /** 判断是否保洁人员 */
    private boolean isCleaner(String roles) {
        return roles != null && roles.contains("CLEANER");
    }

    /** 根据角色推断申请人类型 */
    private String resolveApplicantType(String roles) {
        if (roles == null) {
            return null;
        }
        if (roles.contains("CLEANER")) return "CLEANER";
        if (roles.contains("STUDENT")) return "STUDENT";
        if (roles.contains("REPAIRER")) return "REPAIRER";
        return null;
    }


    /** 发送请假事件到 MQ */
    private void sendLeaveEvent(LeaveRequest req, String eventType) {
        try {
            LeaveEvent event = LeaveEvent.builder()
                    .leaveId(req.getId())
                    .applicantId(req.getApplicantId())
                    .applicantType(req.getApplicantType())
                    .campusId(req.getCampusId())
                    .leaveType(req.getLeaveType())
                    .reason(req.getReason())
                    .startTime(req.getStartTime())
                    .endTime(req.getEndTime())
                    .eventType(eventType)
                    .build();
            String routingKey = switch (eventType) {
                case "APPROVED" -> MQConstants.LEAVE_APPROVED_KEY;
                case "REJECTED" -> MQConstants.LEAVE_REJECTED_KEY;
                case "CANCELLED" -> MQConstants.LEAVE_CANCELLED_KEY;
                default -> MQConstants.LEAVE_SUBMITTED_KEY;
            };
            rabbitTemplate.convertAndSend(MQConstants.TOPIC_EXCHANGE, routingKey, event);
        } catch (Exception e) {
            log.error("发送请假事件失败, leaveId={}, eventType={}", req.getId(), eventType, e);
        }
    }

    @Override
    @Transactional
    public void approve(Long id, String opinion) {
        LeaveRequest req = getById(id);
        if (req == null) throw new BusinessException("请假申请不存在");
        if (!LeaveStatus.PENDING.getCode().equals(req.getStatus())) {
            throw new BusinessException("当前状态不允许审批");
        }
        req.setStatus(LeaveStatus.APPROVED.getCode());
        req.setApproverId(UserContext.getUserId());
        req.setApproveTime(LocalDateTime.now());
        req.setApproveOpinion(opinion);
        updateById(req);
        sendLeaveEvent(req, "APPROVED");
    }

    @Override
    @Transactional
    public void reject(Long id, String reason) {
        LeaveRequest req = getById(id);
        if (req == null) throw new BusinessException("请假申请不存在");
        if (!LeaveStatus.PENDING.getCode().equals(req.getStatus())) {
            throw new BusinessException("当前状态不允许审批");
        }
        req.setStatus(LeaveStatus.REJECTED.getCode());
        req.setApproverId(UserContext.getUserId());
        req.setApproveTime(LocalDateTime.now());
        req.setRejectReason(reason);
        updateById(req);
        sendLeaveEvent(req, "REJECTED");
    }

    @Override
    @Transactional
    public void cancel(Long id) {
        LeaveRequest req = getById(id);
        if (req == null) throw new BusinessException("请假申请不存在");
        if (!LeaveStatus.PENDING.getCode().equals(req.getStatus())
                && !LeaveStatus.APPROVED.getCode().equals(req.getStatus())) {
            throw new BusinessException("当前状态不允许撤销");
        }
        req.setStatus(LeaveStatus.CANCELLED.getCode());
        updateById(req);
        sendLeaveEvent(req, "CANCELLED");
    }
}