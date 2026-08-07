package com.huashui.task.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.huashui.task.domain.repair.RepairSubmitDTO;
import com.huashui.task.domain.pojo.RepairOrder;

public interface RepairOrderService extends IService<RepairOrder> {

    Page<RepairOrder> page(Integer page, Integer size, String status, Long buildingId);

    void submit(RepairSubmitDTO dto);

    void assign(Long id, Long repairerId);

    void startRepair(Long id);

    void complete(Long id, String repairResult, String repairImages);

    void evaluate(Long id, Integer rating);

    void cancel(Long id);

    void exportData(String status, Long buildingId);
}