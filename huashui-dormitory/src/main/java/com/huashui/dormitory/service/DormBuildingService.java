package com.huashui.dormitory.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.huashui.common.response.PageResult;
import com.huashui.dormitory.domain.dto.BuildingConfigDTO;
import com.huashui.dormitory.domain.dto.BuildingCreateDTO;
import com.huashui.dormitory.domain.dto.BuildingPageDTO;
import com.huashui.dormitory.domain.dto.BuildingUpdateDTO;
import com.huashui.dormitory.domain.pojo.DormBuilding;
import com.huashui.dormitory.domain.vo.BuildingDetailVO;
import com.huashui.dormitory.domain.vo.BuildingPageVO;

import java.util.List;

public interface DormBuildingService extends IService<DormBuilding> {



    void create(BuildingCreateDTO dto);

    void update(Long id, BuildingUpdateDTO dto);

    void deleteById(Long id);

    BuildingDetailVO getDetail(Long id);

    void updateConfig(Long id, BuildingConfigDTO dto);

    List<DormBuilding> listEnabledByCampus(Long campusId);

    PageResult<BuildingPageVO> getBuildingsPage(BuildingPageDTO dto);
}