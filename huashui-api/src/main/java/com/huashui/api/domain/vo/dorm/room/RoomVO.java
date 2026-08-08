package com.huashui.api.domain.vo.dorm.room;

import lombok.Data;

@Data
public class RoomVO {


    private Long id;


    /**
     * 校区ID
     */
    private Long campusId;


    /**
     * 楼栋ID
     */
    private Long buildingId;


    /**
     * 房间号
     */
    private String roomNo;

}