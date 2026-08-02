package com.huashui.system.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.huashui.system.domain.dto.DictDataDTO;
import com.huashui.system.domain.pojo.SysDictData;

import java.util.List;

public interface SysDictDataService extends IService<SysDictData> {
    Page<SysDictData> page(Integer page, Integer size, String dictType);
    void create(DictDataDTO dto);
    void update(Long id, DictDataDTO dto);
    List<SysDictData> getByDictType(String dictType);
}