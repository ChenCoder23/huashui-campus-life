package com.huashui.system.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.huashui.system.domain.dto.DictTypeDTO;
import com.huashui.system.domain.pojo.SysDictType;

public interface SysDictTypeService extends IService<SysDictType> {
    Page<SysDictType> page(Integer page, Integer size, String keyword);
    void create(DictTypeDTO dto);
    void update(Long id, DictTypeDTO dto);
}