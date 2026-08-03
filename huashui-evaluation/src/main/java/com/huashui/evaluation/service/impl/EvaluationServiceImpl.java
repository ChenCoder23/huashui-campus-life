package com.huashui.evaluation.service.impl;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.huashui.evaluation.domain.pojo.*;
import com.huashui.evaluation.mapper.*;
import com.huashui.evaluation.service.EvaluationService;
import lombok.RequiredArgsConstructor; import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j @Service @RequiredArgsConstructor
public class EvaluationServiceImpl implements EvaluationService {
    private final EvaluationConfigMapper configMapper;
    private final EvaluationResultMapper resultMapper;
    @Override public Page<EvaluationConfig> configPage(Integer page, Integer size) {
        return configMapper.selectPage(new Page<>(page, size), new LambdaQueryWrapper<EvaluationConfig>().orderByDesc(EvaluationConfig::getCreateTime));
    }
    @Override public Page<EvaluationResult> resultPage(Integer page, Integer size, Long configId) {
        LambdaQueryWrapper<EvaluationResult> qw = new LambdaQueryWrapper<>();
        if (configId != null) qw.eq(EvaluationResult::getConfigId, configId);
        return resultMapper.selectPage(new Page<>(page, size), qw.orderByDesc(EvaluationResult::getCreateTime));
    }
}