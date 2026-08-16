package com.huashui.notification.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.huashui.notification.domain.pojo.Message;
import org.apache.ibatis.annotations.Mapper;

/**
 * 消息 Mapper
 */
@Mapper
public interface MessageMapper extends BaseMapper<Message> {

}