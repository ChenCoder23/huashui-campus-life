package com.huashui.leave.service.impl;

import cn.hutool.core.bean.BeanUtil;
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
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Slf4j
@Service
public class LeaveRequestServiceImpl
        extends ServiceImpl<LeaveRequestMapper, LeaveRequest>
        implements LeaveRequestService {

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
        req.setApplicantId(UserContext.getUserId());
        req.setStatus(LeaveStatus.PENDING.getCode());
        save(req);
        // TODO: RabbitMQ → 通知宿管有新请假申请
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
        // TODO: RabbitMQ → 通知申请人已通过
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
        // TODO: RabbitMQ → 通知申请人已拒绝
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
        // TODO: RabbitMQ → 通知宿管已撤销
    }
}