package com.huashui.dormitory.api;

import com.huashui.api.domain.vo.dorm.room.RoomDetailVO;
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
public class roomAPI {

    private final DormRoomService roomService;

    @GetMapping("/info/{id}")
    public RoomVO getRoomInfoById(@PathVariable Long id){
        return roomService.getRoomInfoById(id);
    }

    @GetMapping("/room/DetailInfo/{id}")
    public RoomDetailVO getRoomDetailInfoById(@PathVariable Long id){
        return roomService.getDetailRoomInfo(id);
    }
}