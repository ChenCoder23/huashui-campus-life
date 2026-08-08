package com.huashui.task.domain.dto.repair;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

@Data
public class RepairCreateDTO {


    /**
     * 房间ID
     */
    @NotNull
    private Long roomId;


    /**
     * 报修类型
     */
    @NotBlank
    private String repairType;


    /**
     * 问题描述
     */
    @NotBlank
    private String description;


    /**
     * 故障图片
     */
    private List<String> images;


    /**
     * 联系电话
     */
    @NotBlank
    private String contactPhone;


    /**
     * 预约维修时间
     */
    private String appointmentTime;

}