package com.huashui.system.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.huashui.common.enums.Status;
import com.huashui.common.exception.BusinessException;
import com.huashui.system.domain.dto.ConfigDTO;
import com.huashui.system.domain.pojo.SysConfig;
import com.huashui.system.mapper.SysConfigMapper;
import com.huashui.system.service.SysConfigService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
public class SysConfigServiceImpl extends ServiceImpl<SysConfigMapper, SysConfig> implements SysConfigService {

    @Override
    public Page<SysConfig> page(Integer page, Integer size, String configGroup) {
        LambdaQueryWrapper<SysConfig> qw = new LambdaQueryWrapper<>();
        if (configGroup != null) qw.eq(SysConfig::getConfigGroup, configGroup);
        qw.orderByAsc(SysConfig::getSortOrder);
        return this.page(new Page<>(page, size), qw);
    }

    @Override
    @Transactional
    public void create(ConfigDTO dto) {
        if (lambdaQuery().eq(SysConfig::getConfigKey, dto.getConfigKey()).count() > 0) {
            throw new BusinessException("配置键已存在");
        }
        SysConfig entity = BeanUtil.copyProperties(dto, SysConfig.class);
        entity.setStatus(Status.ENABLED);
        save(entity);
    }

    @Override
    @Transactional
    public void update(Long id, ConfigDTO dto) {
        SysConfig entity = getById(id);
        if (entity == null) throw new BusinessException("系统配置不存在");
        BeanUtil.copyProperties(dto, entity, "id");
        updateById(entity);
    }

    @Override
    public String getValueByKey(String key) {
        SysConfig config = lambdaQuery()
                .eq(SysConfig::getConfigKey, key)
                .one();

        if(config == null){
            throw new RuntimeException(
                    "系统配置不存在:" + key
            );
        }
        return config.getConfigValue();
    }
}