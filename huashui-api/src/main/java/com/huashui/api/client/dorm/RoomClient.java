package com.huashui.api.client.dorm;

import com.huashui.api.domain.vo.dorm.room.RoomDetailVO;
import com.huashui.api.domain.vo.dorm.room.RoomVO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient("huashui-dormitory")
public interface RoomClient {

    @GetMapping("/room/info/{id}")
    RoomVO getRoomInfoById(@PathVariable Long id);

    @GetMapping("/room/room/DetailInfo/{id}")
    RoomDetailVO getRoomDetailInfoById(@PathVariable Long id);
}