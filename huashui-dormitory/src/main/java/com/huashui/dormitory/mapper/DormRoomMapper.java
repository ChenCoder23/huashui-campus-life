package com.huashui.dormitory.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.huashui.api.domain.vo.dorm.room.RoomVO;
import com.huashui.dormitory.domain.pojo.DormRoom;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface DormRoomMapper extends BaseMapper<DormRoom> {
    RoomVO getRoomInfoById(Long id);

    RoomVO getRoomDetailInfo(Long id);
}