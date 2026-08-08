package com.huashui.evaluation.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.huashui.common.domain.mqMessage.RepairEvent;
import com.huashui.evaluation.domain.pojo.EvaluationRecord;

public interface EvaluationRecordService
        extends IService<EvaluationRecord> {


    /**
     * 创建维修评价
     */
    void createRepairEvaluation(RepairEvent event);

}