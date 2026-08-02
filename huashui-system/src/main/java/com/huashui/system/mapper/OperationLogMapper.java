package com.huashui.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.huashui.system.domain.pojo.OperationLog;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface OperationLogMapper extends BaseMapper<OperationLog> {
}