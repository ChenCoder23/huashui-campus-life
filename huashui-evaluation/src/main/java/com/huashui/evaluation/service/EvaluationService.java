package com.huashui.evaluation.service;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.huashui.evaluation.domain.pojo.*;
public interface EvaluationService {
    Page<EvaluationConfig> configPage(Integer page, Integer size);
    Page<EvaluationResult> resultPage(Integer page, Integer size, Long configId);
}