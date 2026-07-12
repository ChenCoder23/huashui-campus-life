package com.huashui.dormitory.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.huashui.common.exception.BusinessException;
import com.huashui.dormitory.domain.dto.BuildingDTO;
import com.huashui.dormitory.domain.dto.BuildingPageDTO;
import com.huashui.dormitory.domain.pojo.DormBuilding;
import com.huashui.dormitory.domain.vo.BuildingPageVO;
import com.huashui.dormitory.mapper.DormBuildingMapper;
import com.huashui.dormitory.service.DormBuildingService;
import com.huashui.dormitory.service.SysCampusService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class DormBuildingServiceImpl extends ServiceImpl<DormBuildingMapper, DormBuilding> implements DormBuildingService {


    @Autowired
    private SysCampusService campusService;

    //获取楼栋信息列表的分页查询
    @Override
    public void getBuildingPage(BuildingPageDTO dto) {

        // todo 加入redis 缓存 key随便
        LambdaQueryWrapper<DormBuilding> wrapper = Wrappers.lambdaQuery();

        wrapper.like(StrUtil.isNotBlank(dto.getBuildingName()),
                DormBuilding::getBuildingName,
                dto.getBuildingName());

        wrapper.like(StrUtil.isNotBlank(dto.getBuildingCode()),
                DormBuilding::getBuildingCode,
                dto.getBuildingCode());

        wrapper.eq(dto.getStatus() != null,
                DormBuilding::getStatus,
                dto.getStatus());

        wrapper.eq(dto.getCampusId() != null,
                DormBuilding::getCampusId,
                dto.getCampusId());


        wrapper.orderByAsc(DormBuilding::getCampusId)
                .orderByAsc(DormBuilding::getBuildingCode);

        Page<DormBuilding> page = new Page<>(
                dto.getPageNum(),
                dto.getPageSize()
        );

        List<DormBuilding> buildings = page.getRecords();
        //属性copy
        List<BuildingPageVO> buildingPageVOList = BeanUtil.copyToList(buildings, BuildingPageVO.class);
        // todo 设置vo所属校区的名称

        // todo VO的宿舍管理的名字

        //  todo VO 是否有未住满的房间 ,有多少间

        // todo 调用统计每个楼栋未住满的数量并存入redis

        // todo 保证返回

    }


    //添加楼栋
    @Override
    public void addBuilding(BuildingDTO dto) {
        //获取校区id
        Long campusId = dto.getCampusId();
        if (campusId == null){
            throw  new BusinessException("楼栋ID不能为null");
        }

        if (! campusService.isExist(campusId)) {
            throw new BusinessException("所选的校区Id不存在");
        }

        String description = dto.getDescription();

        if (StrUtil.isBlank(description)){
            description = generateDescription(dto);
        }

        //  todo 校验楼栋编码和楼栋名称是否重复

        // todo save
    }


    //修改楼栋基础信息
    @Override
    public void updateBuilding(BuildingDTO dto) {


    }

    private String generateDescription(BuildingDTO dto) {

    }

    // todo 统计每个楼栋未住满的数量并存入redis key 随便

}