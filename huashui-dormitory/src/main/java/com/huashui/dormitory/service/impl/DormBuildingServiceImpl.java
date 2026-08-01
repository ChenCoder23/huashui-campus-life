package com.huashui.dormitory.service.impl;

import cn.hutool.core.bean.BeanUtil;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.huashui.common.enums.Status;
import com.huashui.common.exception.BusinessException;
import com.huashui.common.response.PageResult;
import com.huashui.dormitory.domain.dto.BuildingConfigDTO;
import com.huashui.dormitory.domain.dto.BuildingCreateDTO;
import com.huashui.dormitory.domain.dto.BuildingPageDTO;
import com.huashui.dormitory.domain.dto.BuildingUpdateDTO;
import com.huashui.dormitory.domain.pojo.DormBuilding;
import com.huashui.dormitory.domain.pojo.DormBuildingConfig;
import com.huashui.dormitory.domain.pojo.DormRoom;
import com.huashui.dormitory.domain.vo.BuildingDetailVO;
import com.huashui.dormitory.domain.vo.BuildingPageVO;
import com.huashui.dormitory.mapper.DormBuildingConfigMapper;
import com.huashui.dormitory.mapper.DormBuildingMapper;
import com.huashui.dormitory.mapper.DormRoomMapper;
import com.huashui.dormitory.service.DormBuildingService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class DormBuildingServiceImpl extends ServiceImpl<DormBuildingMapper, DormBuilding> implements DormBuildingService {

    private final DormBuildingConfigMapper configMapper;
    private final DormRoomMapper roomMapper;


    //新增楼栋
    @Override
    @Transactional
    public void create(BuildingCreateDTO dto) {

        String buildingCode = dto.getBuildingCode();
        String buildingName = dto.getBuildingName();
        //判断楼栋名称和编码是否重复
        Long count = lambdaQuery().eq(DormBuilding::getBuildingCode, buildingCode)
                .eq(DormBuilding::getBuildingName, buildingName)
                .count();
        if (count != null && count != 0){
            throw new BusinessException("添加失败该楼栋已存在");
        }
        DormBuilding building = BeanUtil.copyProperties(dto, DormBuilding.class);
        building.setStatus(Status.ENABLED);
        save(building);

        DormBuildingConfig config = BeanUtil.copyProperties(dto, DormBuildingConfig.class);
        config.setBuildingId(building.getId());
        configMapper.insert(config);
    }

    //更新楼栋的配置
    @Override
    @Transactional
    public void update(Long id, BuildingUpdateDTO dto) {
        DormBuilding building = getById(id);
        if (building == null) throw new BusinessException("楼栋不存在");
        BeanUtil.copyProperties(dto, building, "id");
        updateById(building);
    }

    //删除楼栋
    @Override
    @Transactional
    public void deleteById(Long id) {
        // todo 楼栋里含有未封闭的房间,
        if (roomMapper.selectCount(new LambdaQueryWrapper<DormRoom>().eq(DormRoom::getBuildingId, id)) > 0) {
            throw new BusinessException("该楼栋下存在使用中的房间，无法删除");
        }
        removeById(id);
    }

    //获取楼栋详细信息
    @Override
    public BuildingDetailVO getDetail(Long id) {
        DormBuilding building = getById(id);
        if (building == null) throw new BusinessException("楼栋不存在");
        BuildingDetailVO vo = BeanUtil.copyProperties(building, BuildingDetailVO.class);
        DormBuildingConfig config = configMapper.selectOne(
                new LambdaQueryWrapper<DormBuildingConfig>().eq(DormBuildingConfig::getBuildingId, id));
        vo.setConfig(config);
        return vo;
    }

    //更新楼栋配置信息
    @Override
    @Transactional
    public void updateConfig(Long id, BuildingConfigDTO dto) {
        DormBuildingConfig config = configMapper.selectOne(
                new LambdaQueryWrapper<DormBuildingConfig>().eq(DormBuildingConfig::getBuildingId, id));
        if (config == null) throw new BusinessException("楼栋配置不存在");
        BeanUtil.copyProperties(dto, config, "id", "buildingId");
        configMapper.updateById(config);
    }

    //查询指定校区的所有楼栋
    @Override
    public List<DormBuilding> listEnabledByCampus(Long campusId) {
        LambdaQueryWrapper<DormBuilding> qw = new LambdaQueryWrapper<>();
        qw.eq(DormBuilding::getStatus, Status.ENABLED);
        if (campusId != null) qw.eq(DormBuilding::getCampusId, campusId);
        qw.orderByAsc(DormBuilding::getSortOrder);
        return list(qw);
    }

    //分页查询楼栋信息
    @Override
    public PageResult<BuildingPageVO> getBuildingsPage(BuildingPageDTO dto) {

        Page<DormBuilding> page = lambdaQuery()
                .eq(StrUtil.isNotBlank(dto.getBuildingName()),DormBuilding::getBuildingName,dto.getBuildingName())
                .eq(StrUtil.isNotBlank(dto.getBuildingCode()),DormBuilding::getBuildingCode,dto.getBuildingCode())
                .eq(dto.getStatus() != null,DormBuilding::getStatus,dto.getStatus())
                .eq(dto.getCampusId() != null ,DormBuilding::getCampusId,dto.getCampusId())
                .page(dto.toPage());

        List<BuildingPageVO> buildingPageVOList = BeanUtil.copyToList(page.getRecords(), BuildingPageVO.class);


        IPage<BuildingPageVO> voPage =
                page.convert(item -> BeanUtil.copyProperties(item, BuildingPageVO.class));
        // 填充校区名称
        fillCampusName(voPage.getRecords());

        // 填充宿管姓名
        fillManagerName(voPage.getRecords());

        // 填充未住满房间数量
        fillRoomInfo(voPage.getRecords());

        return PageResult.of(voPage.getTotal(),voPage.getPages(),voPage.getSize(),voPage.getRecords());
    }

    //todo
    private void fillRoomInfo(List<BuildingPageVO> records) {
    }

    //todo
    private void fillManagerName(List<BuildingPageVO> records) {

    }

    //todo
    private void fillCampusName(List<BuildingPageVO> records) {

    }
}