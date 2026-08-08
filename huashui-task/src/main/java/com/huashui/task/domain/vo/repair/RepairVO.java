package com.huashui.task.domain.vo.repair;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class RepairVO {


    private Long id;


    /**
     * 工单编号
     */
    private String orderNo;


    /**
     * 报修类型
     */
    private String repairType;



    /**
     * 当前状态
     */
    private String status;


    /**
     * 维修人员
     */
    private String repairerName;


    /**
     * 评分
     */
    private Integer rating;


    private LocalDateTime createTime;

}