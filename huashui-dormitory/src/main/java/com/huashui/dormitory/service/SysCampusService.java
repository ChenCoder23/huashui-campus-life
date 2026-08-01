package com.huashui.dormitory.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.huashui.dormitory.domain.dto.CampusDTO;
import com.huashui.dormitory.domain.pojo.SysCampus;

import java.util.List;

public interface SysCampusService extends IService<SysCampus> {

    Page<SysCampus> page(Integer page, Integer size);

    void create(CampusDTO dto);

    void update(Long id, CampusDTO dto);

    void deleteById(Long id);

    List<SysCampus> listEnabled();
}