package com.huashui.task.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.huashui.common.response.PageResult;
import com.huashui.common.response.Result;
import com.huashui.task.domain.dto.query.RepairQueryDTO;
import com.huashui.task.domain.dto.repair.RepairAssignDTO;
import com.huashui.task.domain.dto.repair.RepairCompleteDTO;
import com.huashui.task.domain.dto.repair.RepairCreateDTO;
import com.huashui.task.domain.pojo.RepairOrder;
import com.huashui.task.domain.vo.repair.RepairVO;
import com.huashui.task.domain.vo.repair.repairDetailVO;

public interface RepairOrderService extends IService<RepairOrder> {



    void createRepairOrder(RepairCreateDTO dto);

    PageResult<RepairVO> getMyRepairPage(RepairQueryDTO dto);

    Result<repairDetailVO> getDetailByOrderId(Long id);

    void cancelOrder(Long id);

    PageResult<RepairVO> adminPage(RepairQueryDTO dto);

    PageResult<RepairVO> workerRepairPage(RepairQueryDTO dto);

    void assign(RepairAssignDTO dto);

    void start(Long id);

    void complete(RepairCompleteDTO dto);
}