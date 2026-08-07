package com.huashui.task.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.huashui.task.domain.pojo.Task;
import org.apache.ibatis.annotations.Mapper;


@Mapper
public interface TaskMapper extends BaseMapper<Task> {


}