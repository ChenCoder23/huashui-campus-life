package com.huashui.dormitory.api;

import com.huashui.api.domain.vo.dorm.room.RoomVO;
import com.huashui.dormitory.service.DormRoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/room")
@RequiredArgsConstructor
public class RoomClient {


    private final DormRoomService roomService;


    //查询基本的房间信息
    @GetMapping("/info/{id}")
    public RoomVO getRoomInfoById(@PathVariable Long id){
        return roomService.getRoomInfoById(id);
    }

    //查询完整的房间信息
    @GetMapping("/room/DetailInfo/{id}")
    RoomVO getRoomDetailInfoById(@PathVariable Long id){
        return roomService.getDetailRoomInfo(id);
    }

}