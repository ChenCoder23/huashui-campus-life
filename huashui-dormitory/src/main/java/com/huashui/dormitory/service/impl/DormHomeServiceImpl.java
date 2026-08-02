package com.huashui.dormitory.service.impl;

import com.huashui.common.exception.BusinessException;
import com.huashui.common.utils.UserContext;
import com.huashui.dormitory.Enum.DormRecordStatus;
import com.huashui.dormitory.domain.pojo.DormStudentRecord;
import com.huashui.dormitory.domain.vo.DormHomeVO;
import com.huashui.dormitory.domain.vo.RoommateVO;
import com.huashui.dormitory.service.DormHomeService;
import com.huashui.dormitory.service.DormStudentRecordService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class DormHomeServiceImpl implements DormHomeService {

    @Autowired
    private DormStudentRecordService recordService;

    @Override
    public DormHomeVO getMyDorm() {

        // todo
        //获取学生的id
        Long userId = UserContext.getUserId();
        //获取学生的房间id和床位id
        DormStudentRecord dormStudentRecord = recordService.lambdaQuery()
                .eq(DormStudentRecord::getStudentId, userId)
                .eq(DormStudentRecord::getStatus, DormRecordStatus.LIVING)
                .one(); //每个学生的在在住记录只有一条
        if (dormStudentRecord == null){
            throw new BusinessException("未查询到当前住宿信息");
        }
        Long roomId = dormStudentRecord.getRoomId();
        Long bedId = dormStudentRecord.getBedId();



        return DormHomeVO.builder().build();
    }

    @Override
    public List<RoommateVO> getMyRoommates() {
        // TODO: 查同房间其他床位 → 通过 Feign 调 rbac 获取姓名
        return List.of();
    }
}