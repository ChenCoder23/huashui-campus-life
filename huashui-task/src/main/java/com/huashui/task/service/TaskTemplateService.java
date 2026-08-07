package com.huashui.task.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.huashui.common.response.PageResult;
import com.huashui.task.domain.dto.template.TaskTemplateDTO;
import com.huashui.task.domain.dto.query.TaskTemplateQueryDTO;
import com.huashui.task.domain.pojo.TaskTemplate;
import com.huashui.task.domain.vo.template.CleanTaskTemplateDetailVO;
import com.huashui.task.domain.vo.template.CleanTaskTemplateVO;


/**
 * 任务模板服务
 */
public interface TaskTemplateService extends IService<TaskTemplate> {


    PageResult<CleanTaskTemplateVO> getPage(TaskTemplateQueryDTO dto);

    void add(TaskTemplateDTO dto);

    void updateTaskPlateDto(TaskTemplateDTO dto);

    void deleteTemplate(Long id);

    CleanTaskTemplateDetailVO detail(Long id);
}