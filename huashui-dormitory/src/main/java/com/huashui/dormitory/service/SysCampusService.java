package com.huashui.dormitory.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.huashui.common.response.PageResult;
import com.huashui.dormitory.domain.dto.CampusDTO;
import com.huashui.dormitory.domain.pojo.SysCampus;

import java.util.List;
import java.util.Map;
import java.util.Set;

public interface SysCampusService extends IService<SysCampus> {

    PageResult<SysCampus> page(Integer page, Integer size);

    void create(CampusDTO dto);

    void update(Long id, CampusDTO dto);

    void deleteById(Long id);

    List<SysCampus> listEnabled();

    Map<Long, String> batchName(Set<Long> ids);
}