package com.huashui.task.domain.vo.template;


import lombok.Data;

import java.time.LocalDateTime;


/**
 * 任务模板VO
 */
@Data
public class CleanTaskTemplateVO {

    /**

     *
     * 使用第一条模板记录id作为标识
     */
    private Long id;


    /**
     * 模板名称
     */
    private String name;


    /**
     * 校区ID
     */
    private Long campusId;


    /**
     * 校区名称
     */
    private String campusName;


    /**
     * 楼栋ID
     */
    private Long buildingId;


    /**
     * 楼栋名称
     */
    private String buildingName;


    /**
     * 是否启用
     */
    private Integer enabled;


    /**
     * 创建时间
     */
    private LocalDateTime createTime;


    /**
     * 更新时间
     */
    private LocalDateTime updateTime;

    /**
     * 任务人数
     */
    private Integer workerCount;

}