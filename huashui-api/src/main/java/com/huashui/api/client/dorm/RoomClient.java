package com.huashui.api.client.dorm;


import com.huashui.api.domain.vo.dorm.room.RoomDetailVO;
import com.huashui.api.domain.vo.dorm.room.RoomVO;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;


/**
 * 宿舍服务远程调用
 */
@FeignClient("huashui-dormitory")
public interface RoomClient {


    /**
     * 根据房间ID查询宿舍信息
     */
    @GetMapping("/room/info/{id}")
    RoomVO getRoomInfoById(@PathVariable Long id);


    @GetMapping("/room/DetailInfo/{id}")
    RoomDetailVO getRoomDetailInfoById(@PathVariable Long id);

}