package com.huashui.dormitory.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.huashui.dormitory.domain.pojo.DormBuilding;
import com.huashui.dormitory.mapper.DormBuildingMapper;
import com.huashui.dormitory.service.DormBuildingService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class DormBuildingServiceImpl
        extends ServiceImpl<DormBuildingMapper, DormBuilding>
        implements DormBuildingService {
}