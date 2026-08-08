package com.huashui.api.domain.vo.dorm.room;


import lombok.Data;


@Data
public class RoomDetailVO {


    /**
     * 房间ID
     */
    private Long roomId;


    /**
     * 房间号
     */
    private String roomNumber;


    /**
     * 楼栋ID
     */
    private Long buildingId;


    /**
     * 楼栋名称
     */
    private String buildingName;


    /**
     * 校区ID
     */
    private Long campusId;


    /**
     * 校区名称
     */
    private String campusName;

}