package com.huashui.dormitory.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.huashui.dormitory.domain.dto.BuildingDTO;
import com.huashui.dormitory.domain.dto.BuildingPageDTO;
import com.huashui.dormitory.domain.pojo.DormBuilding;

public interface DormBuildingService extends IService<DormBuilding> {
    void getBuildingPage(BuildingPageDTO dto);

    void addBuilding(BuildingDTO dto);

    void updateBuilding(BuildingDTO dto);
}