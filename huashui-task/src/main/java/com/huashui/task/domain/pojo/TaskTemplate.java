package com.huashui.task.domain.pojo;


import com.baomidou.mybatisplus.annotation.TableName;
import com.huashui.common.domain.pojo.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;


@Data
@TableName("task_template")
@EqualsAndHashCode(callSuper = true)
public class TaskTemplate extends BaseEntity {


    private Long Id;

    /**
     * 模板名称
     */
    private String name;


    /**
     * 默认负责人ID
     */
    private Long workerId;


    /**
     * 负责人姓名
     */
    private String workerName;


    /**
     * 校区ID
     */
    private Long campusId;


    /**
     * 楼栋ID
     */
    private Long buildingId;


    /**
     * 工作区域描述
     */
    private String areaDesc;


    /**
     * 任务内容
     */
    private String taskContent;


    /**
     * 是否启用
     */
    private Integer enabled;

}