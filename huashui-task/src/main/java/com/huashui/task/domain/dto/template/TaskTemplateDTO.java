package com.huashui.task.domain.dto.template;

import lombok.Data;

import java.util.List;


/**
 * 任务模板新增DTO
 */
@Data
public class TaskTemplateDTO {


    /**
     * 模板名称
     */
    private String name;


    private Long campusId;


    private Long buildingId;


    /**
     * 模板任务列表
     */
    private List<TaskTemplateItemDTO> items;


}

