package com.huashui.leave.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.huashui.leave.domain.pojo.LeaveRequest;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface LeaveRequestMapper extends BaseMapper<LeaveRequest> {
}