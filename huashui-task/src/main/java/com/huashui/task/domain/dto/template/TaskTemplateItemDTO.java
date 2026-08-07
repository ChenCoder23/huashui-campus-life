package com.huashui.task.domain.dto.template;

import lombok.Data;

import java.time.LocalDateTime;

@Data
    public  class TaskTemplateItemDTO {


        private Long workerId;


        private String workerName;


        private String areaDesc;


        private String taskContent;

        private LocalDateTime  deadline;

    }