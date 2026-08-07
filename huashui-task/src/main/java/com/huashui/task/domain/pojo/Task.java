package com.huashui.task.domain.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.huashui.task.Enums.CleanTaskStatus;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 任务表
 */
@Data
@TableName("task")
public class Task {


    /**
     * 任务ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;


    /**
     * 任务标题
     */
    private String title;


    /**
     * 任务内容
     */
    private String content;


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
    private Long campusId;


    /**
     * 楼栋ID
     */
    private Long buildingId;


    /**
     * 任务状态
     * TODO 待执行
     * DOING 执行中
     * COMPLETED 已完成
     * CANCELLED 已取消
     */
    private CleanTaskStatus status;


    /**
     * 截止时间
     */
    private LocalDateTime deadline;


    /**
     * 完成时间
     */
    private LocalDateTime finishTime;


    /**
     * 完成照片(JSON数组)
     */
    private String imageUrls;


    /**
     * 完成备注
     */
    private String remark;

    /**
     * 任务执行时间
     */
    private LocalDate taskDate;


    /**
     * 创建时间
     */
    private LocalDateTime createTime;


    /**
     * 更新时间
     */
    private LocalDateTime updateTime;

}