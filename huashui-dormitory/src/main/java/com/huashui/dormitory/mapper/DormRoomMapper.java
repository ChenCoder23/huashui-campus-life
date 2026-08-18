package com.huashui.dormitory.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.huashui.api.domain.vo.dorm.room.RoomDetailVO;
import com.huashui.api.domain.vo.dorm.room.RoomVO;
import com.huashui.dormitory.domain.pojo.DormRoom;
import org.apache.ibatis.annotations.MapKey;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Map;

@Mapper
public interface DormRoomMapper extends BaseMapper<DormRoom> {
    RoomVO getRoomInfoById(Long id);

    RoomDetailVO getRoomDetailInfo(Long id);

    @MapKey("buildingId")
    List<Map<String, Object>> countAvailableRooms(@Param("buildingIds") List<Long> buildingIds);
}