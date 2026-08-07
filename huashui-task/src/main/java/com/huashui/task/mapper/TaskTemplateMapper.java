package com.huashui.task.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.huashui.task.domain.dto.query.TaskTemplateQueryDTO;
import com.huashui.task.domain.pojo.TaskTemplate;
import com.huashui.task.domain.vo.template.CleanTaskTemplateVO;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.data.repository.query.Param;


/**
 * 任务模板Mapper
 */
@Mapper
public interface TaskTemplateMapper extends BaseMapper<TaskTemplate> {

    /**
     * 分页查询模板
     */
    Page<CleanTaskTemplateVO> selectTemplatePage(Page<CleanTaskTemplateVO> page, @Param("dto") TaskTemplateQueryDTO dto);
}