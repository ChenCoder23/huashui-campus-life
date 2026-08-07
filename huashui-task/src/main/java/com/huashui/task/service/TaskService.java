package com.huashui.task.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.huashui.common.response.PageResult;
import com.huashui.task.domain.dto.cleanTask.UpdateTaskStatusDTO;
import com.huashui.task.domain.dto.query.CleanTaskQueryDTO;
import com.huashui.task.domain.dto.template.TaskTemplateDTO;
import com.huashui.task.domain.pojo.Task;
import com.huashui.task.domain.vo.cleanTask.CleanTaskDetailVO;
import com.huashui.task.domain.vo.cleanTask.CleanTaskVO;

import java.util.List;


public interface TaskService extends IService<Task> {


    void generate(TaskTemplateDTO dto);

    PageResult<CleanTaskVO> getCleanTaskPage(CleanTaskQueryDTO dto);

    List<CleanTaskVO> getTodayCleanTask();

    CleanTaskDetailVO getDetail(Long id);

    void updateStatus(UpdateTaskStatusDTO dto);
}