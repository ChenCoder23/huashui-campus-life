package com.huashui.task.domain.dto.query;


import com.huashui.common.domain.query.PageQuery;
import lombok.Data;


/**
 * 任务模板查询DTO
 */
@Data
public class TaskTemplateQueryDTO extends PageQuery {


    /**
     * 模板名称
     */
    private String name;


    /**
     * 校区ID
     */
    private Long campusId;


    /**
     * 楼栋ID
     */
    private Long buildingId;


    /**
     * 是否启用
     * 1启用 0禁用
     */
    private Integer enabled;

}