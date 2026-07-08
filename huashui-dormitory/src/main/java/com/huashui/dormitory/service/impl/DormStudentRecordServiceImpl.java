package com.huashui.dormitory.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.huashui.dormitory.domain.pojo.DormStudentRecord;
import com.huashui.dormitory.mapper.DormStudentRecordMapper;
import com.huashui.dormitory.service.DormStudentRecordService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class DormStudentRecordServiceImpl
        extends ServiceImpl<DormStudentRecordMapper, DormStudentRecord>
        implements DormStudentRecordService {
}