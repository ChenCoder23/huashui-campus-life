package com.huashui.task.domain.dto.repair;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;


@Data
public class RepairCompleteDTO {


    /**
     * 工单ID
     */
    @NotNull(message = "工单ID不能为空")
    private Long repairId;



    /**
     * 维修处理结果
     */
    private String repairResult;



    /**
     * 维修后图片
     */
    private List<String> repairImages;

}