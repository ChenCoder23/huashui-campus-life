package com.huashui.task.domain.vo.template;


import com.huashui.task.domain.dto.template.TaskTemplateItemDTO;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;


/**
 * 任务模板详情VO
 */
@Data
public class CleanTaskTemplateDetailVO {


    /**
     * 模板ID
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
     * 模板任务列表
     */
    private List<TaskTemplateItemDTO> items;

}