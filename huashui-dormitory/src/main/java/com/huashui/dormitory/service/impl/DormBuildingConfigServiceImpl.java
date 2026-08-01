package com.huashui.dormitory.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.huashui.dormitory.domain.pojo.DormBuildingConfig;
import com.huashui.dormitory.mapper.DormBuildingConfigMapper;
import com.huashui.dormitory.service.DormBuildingConfigService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class DormBuildingConfigServiceImpl extends ServiceImpl<DormBuildingConfigMapper, DormBuildingConfig> implements DormBuildingConfigService {
}