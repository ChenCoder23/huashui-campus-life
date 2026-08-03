package com.huashui.leave.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.huashui.leave.domain.dto.LeaveSubmitDTO;
import com.huashui.leave.domain.pojo.LeaveRequest;

public interface LeaveRequestService extends IService<LeaveRequest> {

    Page<LeaveRequest> page(Integer page, Integer size, String status);

    void submit(LeaveSubmitDTO dto);

    void approve(Long id, String opinion);

    void reject(Long id, String reason);

    void cancel(Long id);
}