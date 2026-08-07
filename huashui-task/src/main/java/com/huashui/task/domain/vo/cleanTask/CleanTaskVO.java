package com.huashui.task.domain.vo.cleanTask;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CleanTaskVO {


    /**
     * 任务ID
     */
    private Long id;


    /**
     * 任务标题
     */
    private String title;


    /**
     * 执行人员ID
     */
    private Long workerId;


    /**
     * 执行人员姓名
     */
    private String workerName;


    /**
     * 创建人ID
     */
    private Long creatorId;


    /**
     * 创建人姓名
     */
    private String creatorName;


    /**
     * 校区ID
     */
    private String campusId;

    /**
     * 校区名称
     */
    private String campusName;


    /**
     * 楼栋ID
     */
    private String buildingId;


    /**
     * 楼栋名称
     */
    private String buildingName;


    /**
     * 任务状态
     */
    private String status;


    /**
     * 截止时间
     */
    private LocalDateTime deadline;


    /**
     * 完成时间
     */
    private LocalDateTime finishTime;




}