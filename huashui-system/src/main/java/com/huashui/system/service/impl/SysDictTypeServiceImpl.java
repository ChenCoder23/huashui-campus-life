package com.huashui.system.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.huashui.common.enums.Status;
import com.huashui.common.exception.BusinessException;
import com.huashui.system.domain.dto.DictTypeDTO;
import com.huashui.system.domain.pojo.SysDictType;
import com.huashui.system.mapper.SysDictTypeMapper;
import com.huashui.system.service.SysDictTypeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
public class SysDictTypeServiceImpl
        extends ServiceImpl<SysDictTypeMapper, SysDictType>
        implements SysDictTypeService {

    @Override
    public Page<SysDictType> page(Integer page, Integer size, String keyword) {
        return lambdaQuery()
                .like(StrUtil.isNotBlank(keyword), SysDictType::getDictName, keyword)
                .or().like(StrUtil.isNotBlank(keyword), SysDictType::getDictType, keyword)
                .orderByAsc(SysDictType::getId)
                .page(new Page<>(page, size));
    }

    @Override
    @Transactional
    public void create(DictTypeDTO dto) {
        if (lambdaQuery().eq(SysDictType::getDictType, dto.getDictType()).count() > 0) {
            throw new BusinessException("字典类型编码已存在");
        }
        SysDictType entity = BeanUtil.copyProperties(dto, SysDictType.class);
        entity.setStatus(Status.ENABLED);
        save(entity);
    }

    @Override
    @Transactional
    public void update(Long id, DictTypeDTO dto) {
        SysDictType entity = getById(id);
        if (entity == null) throw new BusinessException("字典类型不存在");
        BeanUtil.copyProperties(dto, entity, "id");
        updateById(entity);
    }
}