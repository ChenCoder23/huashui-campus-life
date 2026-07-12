package com.huashui.dormitory.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.huashui.dormitory.domain.pojo.SysCampus;
import com.huashui.dormitory.mapper.SysCampusMapper;
import com.huashui.dormitory.service.SysCampusService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class SysCampusServiceImpl extends ServiceImpl<SysCampusMapper, SysCampus> implements SysCampusService {



    //判断校区是否存在
    @Override
    public boolean isExist(Long campusId) {
        SysCampus campus = getById(campusId);
        return campus != null;
    }
}