package com.huashui.repair.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.huashui.common.exception.BusinessException;
import com.huashui.common.utils.UserContext;
import com.huashui.repair.domain.dto.RepairSubmitDTO;
import com.huashui.repair.domain.pojo.RepairOrder;
import com.huashui.repair.enums.RepairStatus;
import com.huashui.repair.mapper.RepairOrderMapper;
import com.huashui.repair.service.RepairOrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Slf4j
@Service
public class RepairOrderServiceImpl
        extends ServiceImpl<RepairOrderMapper, RepairOrder>
        implements RepairOrderService {

    @Override
    public Page<RepairOrder> page(Integer page, Integer size, String status, Long buildingId) {
        LambdaQueryWrapper<RepairOrder> qw = new LambdaQueryWrapper<>();
        Long userId = UserContext.getUserId();
        String roles = UserContext.getRoles();

        if (roles != null && roles.contains("STUDENT")) {
            qw.eq(RepairOrder::getStudentId, userId);
        } else if (roles != null && roles.contains("REPAIRER")) {
            qw.eq(RepairOrder::getRepairerId, userId);
        } else if (roles != null && roles.contains("DORM_MANAGER") && buildingId != null) {
            qw.eq(RepairOrder::getBuildingId, buildingId);
        }

        if (StrUtil.isNotBlank(status)) qw.eq(RepairOrder::getStatus, status);
        qw.orderByDesc(RepairOrder::getCreateTime);
        return this.page(new Page<>(page, size), qw);
    }

    @Override
    @Transactional
    public void submit(RepairSubmitDTO dto) {
        RepairOrder order = BeanUtil.copyProperties(dto, RepairOrder.class);
        order.setOrderNo("REP" + IdUtil.fastSimpleUUID().substring(0, 12).toUpperCase());
        order.setStudentId(UserContext.getUserId());
        order.setStatus(RepairStatus.PENDING.getCode());
        save(order);
        // TODO: RabbitMQ → 通知宿管有新工单
    }

    @Override
    @Transactional
    public void assign(Long id, Long repairerId) {
        RepairOrder order = getById(id);
        if (order == null) throw new BusinessException("工单不存在");
        if (!RepairStatus.PENDING.getCode().equals(order.getStatus())) {
            throw new BusinessException("当前状态不允许派单");
        }
        order.setRepairerId(repairerId);
        order.setAssignerId(UserContext.getUserId());
        order.setAssignedTime(LocalDateTime.now());
        order.setStatus(RepairStatus.ASSIGNED.getCode());
        updateById(order);
        // TODO: RabbitMQ → 通知学生已派单 + 通知维修工有新任务
    }

    @Override
    @Transactional
    public void startRepair(Long id) {
        RepairOrder order = getById(id);
        if (order == null) throw new BusinessException("工单不存在");
        if (!RepairStatus.ASSIGNED.getCode().equals(order.getStatus())) {
            throw new BusinessException("当前状态不允许接单");
        }
        order.setStatus(RepairStatus.REPAIRING.getCode());
        updateById(order);
        // TODO: RabbitMQ → 通知学生维修进行中
    }

    @Override
    @Transactional
    public void complete(Long id, String repairResult, String repairImages) {
        RepairOrder order = getById(id);
        if (order == null) throw new BusinessException("工单不存在");
        if (!RepairStatus.REPAIRING.getCode().equals(order.getStatus())) {
            throw new BusinessException("当前状态不允许完成");
        }
        order.setStatus(RepairStatus.COMPLETED.getCode());
        order.setRepairResult(repairResult);
        order.setRepairImages(repairImages);
        order.setRepairTime(LocalDateTime.now());
        updateById(order);
        // TODO: RabbitMQ → 通知学生评价 + 通知宿管已完成
    }

    @Override
    @Transactional
    public void evaluate(Long id, Integer rating) {
        RepairOrder order = getById(id);
        if (order == null) throw new BusinessException("工单不存在");
        if (!RepairStatus.COMPLETED.getCode().equals(order.getStatus())) {
            throw new BusinessException("当前状态不允许评价");
        }
        if (rating < 1 || rating > 5) throw new BusinessException("评分范围 1-5");
        order.setRating(rating);
        order.setStatus(RepairStatus.EVALUATED.getCode());
        updateById(order);
        // TODO: RabbitMQ → 通知维修工收到评价
    }

    @Override
    @Transactional
    public void cancel(Long id) {
        RepairOrder order = getById(id);
        if (order == null) throw new BusinessException("工单不存在");
        if (RepairStatus.EVALUATED.getCode().equals(order.getStatus())
                || RepairStatus.CANCELLED.getCode().equals(order.getStatus())) {
            throw new BusinessException("当前状态不允许取消");
        }
        order.setStatus(RepairStatus.CANCELLED.getCode());
        updateById(order);
        // TODO: RabbitMQ → 通知学生工单已取消
    }

    @Override
    public void exportData(String status, Long buildingId) {
        // TODO: EasyExcel 导出工单数据
    }
}