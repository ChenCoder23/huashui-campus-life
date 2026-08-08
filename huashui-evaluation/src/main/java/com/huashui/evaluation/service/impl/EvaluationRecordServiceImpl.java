package com.huashui.evaluation.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.huashui.common.domain.mqMessage.RepairEvent;
import com.huashui.evaluation.Enums.EvaluationStatusEnum;
import com.huashui.evaluation.Enums.EvaluationTargetTypeEnum;
import com.huashui.evaluation.domain.pojo.EvaluationRecord;
import com.huashui.evaluation.domain.pojo.EvaluationTemplate;
import com.huashui.evaluation.mapper.EvaluationRecordMapper;
import com.huashui.evaluation.mapper.EvaluationTemplateMapper;
import com.huashui.evaluation.service.EvaluationRecordService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class EvaluationRecordServiceImpl extends ServiceImpl<EvaluationRecordMapper, EvaluationRecord> implements EvaluationRecordService {


    private final EvaluationTemplateMapper templateMapper;



    @Override
    @Transactional
    public void createRepairEvaluation(RepairEvent event){

        //1. 幂等判断 ,将工单id作为业务id
        boolean exists =
                lambdaQuery()
                .eq(EvaluationRecord::getTargetType, "REPAIR_ORDER")
                .eq(EvaluationRecord::getTargetId, event.getRepairId())
                .exists();
        if(exists){
            return;
        }
        //2. 查询维修评价模板
        EvaluationTemplate template = templateMapper.selectOne(
                        Wrappers.lambdaQuery(EvaluationTemplate.class)
                        .eq(EvaluationTemplate::getType, "REPAIR")
                        .eq(EvaluationTemplate::getStatus, "ACTIVE"));
        if(template == null){
            throw new RuntimeException("不存在维修评价模板");
        }
        //3. 创建评价记录
        EvaluationRecord record = new EvaluationRecord();
        record.setTemplateId(template.getId());
        record.setEvaluatorId(event.getStudentId());
        record.setTargetType(EvaluationTargetTypeEnum.REPAIR_ORDER);
        record.setTargetId(event.getRepairId());
        record.setTargetUserId(event.getRepairerId());
        record.setBuildingId(event.getBuildingId());
        record.setRoomId(event.getRoomId());
        record.setStatus(EvaluationStatusEnum.WAITING);
        save(record);
    }

}