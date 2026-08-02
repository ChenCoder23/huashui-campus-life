package com.huashui.system.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.huashui.common.enums.Status;
import com.huashui.common.exception.BusinessException;
import com.huashui.system.domain.dto.DictDataDTO;
import com.huashui.system.domain.pojo.SysDictData;
import com.huashui.system.mapper.SysDictDataMapper;
import com.huashui.system.service.SysDictDataService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
public class SysDictDataServiceImpl
        extends ServiceImpl<SysDictDataMapper, SysDictData>
        implements SysDictDataService {

    @Override
    public Page<SysDictData> page(Integer page, Integer size, String dictType) {
        LambdaQueryWrapper<SysDictData> qw = new LambdaQueryWrapper<>();
        if (dictType != null) qw.eq(SysDictData::getDictType, dictType);
        qw.orderByAsc(SysDictData::getSortOrder);
        return this.page(new Page<>(page, size), qw);
    }

    @Override
    @Transactional
    public void create(DictDataDTO dto) {
        SysDictData entity = BeanUtil.copyProperties(dto, SysDictData.class);
        entity.setStatus(Status.ENABLED);
        save(entity);
    }

    @Override
    @Transactional
    public void update(Long id, DictDataDTO dto) {
        SysDictData entity = getById(id);
        if (entity == null) throw new BusinessException("字典数据不存在");
        BeanUtil.copyProperties(dto, entity, "id");
        updateById(entity);
    }

    @Override
    public List<SysDictData> getByDictType(String dictType) {
        return lambdaQuery()
                .eq(SysDictData::getDictType, dictType)
                .eq(SysDictData::getStatus, Status.ENABLED)
                .orderByAsc(SysDictData::getSortOrder)
                .list();
    }
}