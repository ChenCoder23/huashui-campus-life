package com.huashui.evaluation.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.huashui.evaluation.domain.pojo.EvaluationScore;
import com.huashui.evaluation.mapper.EvaluationScoreMapper;
import com.huashui.evaluation.service.EvaluationScoreService;
import org.springframework.stereotype.Service;

@Service
public class EvaluationScoreServiceImpl
        extends ServiceImpl<
                EvaluationScoreMapper,
                EvaluationScore>
        implements EvaluationScoreService {


}