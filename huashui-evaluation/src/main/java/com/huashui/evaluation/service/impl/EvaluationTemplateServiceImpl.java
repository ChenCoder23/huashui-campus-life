package com.huashui.evaluation.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.huashui.evaluation.domain.pojo.EvaluationTemplate;
import com.huashui.evaluation.mapper.EvaluationTemplateMapper;
import com.huashui.evaluation.service.EvaluationTemplateService;
import org.springframework.stereotype.Service;

@Service
public class EvaluationTemplateServiceImpl
        extends ServiceImpl<
                EvaluationTemplateMapper,
                EvaluationTemplate>
        implements EvaluationTemplateService {


}