package com.huashui.system.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.huashui.system.domain.dto.ConfigDTO;
import com.huashui.system.domain.pojo.SysConfig;

public interface SysConfigService extends IService<SysConfig> {
    Page<SysConfig> page(Integer page, Integer size, String configGroup);
    void create(ConfigDTO dto);
    void update(Long id, ConfigDTO dto);
}