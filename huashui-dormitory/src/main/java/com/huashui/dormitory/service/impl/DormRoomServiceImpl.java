package com.huashui.dormitory.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.huashui.dormitory.domain.pojo.DormRoom;
import com.huashui.dormitory.mapper.DormRoomMapper;
import com.huashui.dormitory.service.DormRoomService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class DormRoomServiceImpl
        extends ServiceImpl<DormRoomMapper, DormRoom>
        implements DormRoomService {
}