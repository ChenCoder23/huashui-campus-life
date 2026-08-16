package com.huashui.dormitory.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.huashui.common.enums.Status;
import com.huashui.common.exception.BusinessException;
import com.huashui.common.response.PageResult;
import com.huashui.dormitory.domain.dto.CampusDTO;
import com.huashui.dormitory.domain.pojo.DormBuilding;
import com.huashui.dormitory.domain.pojo.SysCampus;
import com.huashui.dormitory.mapper.DormBuildingMapper;
import com.huashui.dormitory.mapper.SysCampusMapper;
import com.huashui.dormitory.service.SysCampusService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class SysCampusServiceImpl
        extends ServiceImpl<SysCampusMapper, SysCampus>
        implements SysCampusService {

    private final DormBuildingMapper buildingMapper;

    @Override
    public PageResult<SysCampus> page(Integer page, Integer size) {
        Page<SysCampus> paged = lambdaQuery().orderByAsc(SysCampus::getSortOrder).page(new Page<>(page, size));
        return PageResult.of(paged.getTotal(),paged.getPages(),paged.getSize(),paged.getRecords());
    }

    @Override
    public void create(CampusDTO dto) {
        SysCampus campus = BeanUtil.copyProperties(dto, SysCampus.class);
        campus.setStatus(Status.ENABLED);
        try {
            boolean save = save(campus);
        } catch (BusinessException e) {
            throw new BusinessException("新增失败检查校区编码是否重复") ;
        }
    }

    @Override
    @Transactional
    public void update(Long id, CampusDTO dto) {
        SysCampus campus = getById(id);
        if (campus == null) throw new BusinessException("校区不存在");
        BeanUtil.copyProperties(dto, campus);
        updateById(campus);
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        if (buildingMapper.selectCount(new LambdaQueryWrapper<DormBuilding>().eq(DormBuilding::getCampusId, id)) > 0) {
            throw new BusinessException("该校区下存在使用中的楼栋，无法删除");
        }
        SysCampus campus = getById(id);
        if (campus == null) throw new BusinessException("校区不存在");
        campus.setStatus(Status.DISABLED);
        updateById(campus);
    }

    @Override
    public List<SysCampus> listEnabled() {
        return lambdaQuery().eq(SysCampus::getStatus, Status.ENABLED).orderByAsc(SysCampus::getSortOrder).list();
    }


    //根据id获取校区的名称
    @Override
    public Map<Long, String> batchName(Set<Long> ids) {
        return lambdaQuery()
                .in(SysCampus::getId, ids)
                .list()
                .stream()
                .collect(
                        Collectors.toMap(
                                SysCampus::getId,
                                SysCampus::getCampusName
                        )
                );
    }
}