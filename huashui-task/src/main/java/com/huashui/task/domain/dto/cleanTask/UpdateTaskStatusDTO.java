package com.huashui.task.domain.dto.cleanTask;


import com.huashui.task.Enums.CleanTaskStatus;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class UpdateTaskStatusDTO {

    /**
     * 任务ID
     */
    @NotNull(message = "任务ID不能为空")
    private Long taskId;


    /**
     * 任务状态
     */
    @NotNull(message = "任务状态不能为空")
    private CleanTaskStatus status;


    /**
     * 完成照片(JSON数组)
     */
    private String imageUrls;


    /**
     * 完成任务备注 ,如果为空自动生成 who + 时间  + " 完成任务"
     */
    private String remark;
}