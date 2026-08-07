package com.huashui.attendance.domain.dto;

import com.huashui.attendance.enums.CheckInType;
import lombok.Data;

@Data
public class CheckInDTO {


    private Long workerId;


    /**
     * GPS / PHOTO
     */
    private CheckInType checkInType;


    private String location;


    private String photoUrl;

}