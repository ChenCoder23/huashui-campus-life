package com.huashui.task.domain.repair;

import lombok.Data;

/**
 * 维修工单派单DTO
 */
@Data
public class RepairAssignDTO {


    /**
     * 报修工单ID
     */
    private Long repairId;


    /**
     * 维修人员ID
     */
    private Long repairerId;


    /**
     * 维修人员姓名
     */
    private String repairerName;

}