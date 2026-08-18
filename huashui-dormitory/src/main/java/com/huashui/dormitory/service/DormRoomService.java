package com.huashui.dormitory.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.huashui.api.domain.vo.dorm.room.RoomDetailVO;
import com.huashui.api.domain.vo.dorm.room.RoomVO;
import com.huashui.common.response.PageResult;
import com.huashui.dormitory.domain.dto.RoomBatchCreateDTO;
import com.huashui.dormitory.domain.dto.RoomCreateDTO;
import com.huashui.dormitory.domain.dto.RoomPageDTO;
import com.huashui.dormitory.domain.dto.RoomUpdateDTO;
import com.huashui.dormitory.domain.pojo.DormRoom;

public interface DormRoomService extends IService<DormRoom> {

    void create(RoomCreateDTO dto);

    void update(Long id, RoomUpdateDTO dto);

    void deleteById(Long id);

    DormRoom getDetail(Long id);

    void batchCreate(RoomBatchCreateDTO dto);

    void updateBeds(Long roomId, String bedConfig);

    void exportData(Long buildingId, Integer floorNumber, String status);

    PageResult<DormRoom> getRoomPage(RoomPageDTO dto);

    RoomVO getRoomInfoById(Long id);

    RoomDetailVO getDetailRoomInfo(Long id);
}