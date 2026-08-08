package com.huashui.evaluation.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.huashui.evaluation.domain.pojo.EvaluationDimension;
import com.huashui.evaluation.mapper.EvaluationDimensionMapper;
import com.huashui.evaluation.service.EvaluationDimensionService;
import org.springframework.stereotype.Service;

@Service
public class EvaluationDimensionServiceImpl
        extends ServiceImpl<
                EvaluationDimensionMapper,
                EvaluationDimension>
        implements EvaluationDimensionService {


}