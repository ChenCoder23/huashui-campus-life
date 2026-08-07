package com.huashui.attendance.service.impl;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.huashui.api.client.system.SystemClient;
import com.huashui.attendance.domain.dto.AttendanceQueryDTO;
import com.huashui.attendance.domain.dto.AttendanceStatusUpdateDTO;
import com.huashui.attendance.domain.dto.CheckInDTO;
import com.huashui.attendance.domain.pojo.AttendanceRecord;
import com.huashui.attendance.domain.vo.AttendanceStatisticsVO;
import com.huashui.attendance.domain.vo.AttendanceVO;
import com.huashui.attendance.enums.AttendanceStatus;
import com.huashui.attendance.mapper.AttendanceRecordMapper;
import com.huashui.attendance.service.AttendanceRecordService;
import com.huashui.common.exception.BusinessException;
import com.huashui.common.response.PageResult;
import com.huashui.common.response.Result;
import com.huashui.common.utils.UserContext;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class AttendanceRecordServiceImpl extends ServiceImpl<AttendanceRecordMapper, AttendanceRecord> implements AttendanceRecordService {


    private final SystemClient systemClient;


    //清洁工的每日签到
    @Override
    @Transactional
    public void checkIn(CheckInDTO dto) {
        //查询签到记录
        AttendanceRecord record = lambdaQuery()
                .eq(AttendanceRecord::getWorkerId, dto.getWorkerId())
                .eq(AttendanceRecord::getAttendanceDate, LocalDate.now())
                .one();

        //是否生成考勤记录
        if(record == null){
            throw new RuntimeException(
                    "今日未生成考勤记录"
            );
        }
        //判断是否已经签到
        if(record.getCheckInTime()!=null){
            throw new RuntimeException(
                    "今日已经签到"
            );
        }
        // 获取系统配置的迟到时间
        Result<String> result = systemClient.getConfigValue(
                "attendance_late_time"
        );

        LocalTime lateTime = LocalTime.parse(result.getData());
        LocalTime now = LocalTime.now();

        AttendanceStatus status;

        if(now.isAfter(lateTime)){
            status = AttendanceStatus.LATE;
        }else{
            status = AttendanceStatus.NORMAL;
        }
        //设置签到时间
        record.setCheckInTime(LocalDateTime.now());
        //设置签到类型
        record.setCheckInType(dto.getCheckInType().name());
        //设置签到地点
        record.setCheckInLocation(dto.getLocation());
        //设置签到照片
        record.setCheckInPhoto(dto.getPhotoUrl());
        //设置前端状态
        record.setCheckInStatus(status);
        //保存到数据库
        updateById(record);
    }

    @Override
    public AttendanceVO getTodayAttendance(Long workerId) {
        //获取今天日期
        LocalDate today = LocalDate.now();
        //查询今天考勤记录
        AttendanceRecord record =
                lambdaQuery()
                        .eq(AttendanceRecord::getWorkerId, workerId)
                        .eq(AttendanceRecord::getAttendanceDate, today)
                        .one();
        //今天没有签到
        if(record == null){
            throw new RuntimeException("今日无需签到");
        }
        //返回VO
        return convertVO(record);
    }




    /**
     * 考勤统计
     */
    @Override
    public AttendanceStatisticsVO statistics(AttendanceQueryDTO dto) {
        List<AttendanceRecord> list =
                lambdaQuery()
                        .eq(dto.getCampusId()!=null,
                                AttendanceRecord::getCampusId,
                                dto.getCampusId())
                        .eq(dto.getBuildingId()!=null,
                                AttendanceRecord::getBuildingId,
                                dto.getBuildingId())

                        .ge(dto.getStartDate()!=null,
                                AttendanceRecord::getAttendanceDate,
                                dto.getStartDate())
                        .le(dto.getEndDate()!=null,
                                AttendanceRecord::getAttendanceDate,
                                dto.getEndDate())
                        .list();

        AttendanceStatisticsVO vo = new AttendanceStatisticsVO();
        //总记录数
        vo.setTotal(list.size());
        //正常签到数量
        vo.setNormal(countStatus(list, AttendanceStatus.NORMAL));
        //迟到的数量
        vo.setLate(countStatus(list, AttendanceStatus.LATE));
        //缺席的数量
        vo.setAbsent(countStatus(list, AttendanceStatus.ABSENT));
        //请假的数量
        vo.setLeave(countStatus(list, AttendanceStatus.LEAVE));
        return vo;

    }


    @Override
    public List<AttendanceVO> getMyAttendance() {
        // 获取当前登录用户id
        Long workerId = UserContext.getUserId();
        List<AttendanceRecord> records = lambdaQuery()
                        .eq(AttendanceRecord::getWorkerId, workerId)
                        .orderByDesc(AttendanceRecord::getAttendanceDate)
                        .list();
        //VO转换
        return records.stream()
                .map(this::convertVO)
                .collect(Collectors.toList());
    }

    @Override
    public PageResult<AttendanceVO> pageQuery(AttendanceQueryDTO dto) {
        //构造分页参数
        Page<AttendanceRecord> page = new Page<>(dto.getPageNum(), dto.getPageSize());

        // 构造查询条件
        LambdaQueryWrapper<AttendanceRecord> wrapper =  new LambdaQueryWrapper<>();

        //员工
        wrapper.eq(
                dto.getWorkerId()!=null,
                AttendanceRecord::getWorkerId,
                dto.getWorkerId());
        //校区
        wrapper.eq(
                dto.getCampusId()!=null,
                AttendanceRecord::getCampusId,
                dto.getCampusId());
        //楼栋
        wrapper.eq(
                dto.getBuildingId()!=null,
                AttendanceRecord::getBuildingId,
                dto.getBuildingId());
        //状态
        wrapper.eq(
                dto.getStatus()!=null,
                AttendanceRecord::getCheckInStatus,
                dto.getStatus());
        //开始日期
        wrapper.ge(
                dto.getStartDate()!=null,
                AttendanceRecord::getAttendanceDate,
                dto.getStartDate());
        //结束日期
        wrapper.le(
                dto.getEndDate()!=null,
                AttendanceRecord::getAttendanceDate,
                dto.getEndDate());


        //排序
        wrapper.orderByDesc(
                AttendanceRecord::getAttendanceDate
        );

        Page<AttendanceRecord> result = page(page, wrapper);

      // VO转换
        List<AttendanceVO> records =
                result.getRecords()
                        .stream()
                        .map(this::convertVO)
                        .toList();

        return PageResult.of(
                result.getTotal(),
                dto.getPageNum(),
                dto.getPageSize(),
                records);

    }

    @Override
    public void updateAttendance(AttendanceStatusUpdateDTO dto) {
        //查询考勤记录
        AttendanceRecord record = getById(dto.getWorkerId());

        if(record == null){
            throw new BusinessException(
                    "考勤记录不存在"
            );

        }

        //修改状态
        record.setCheckInStatus(dto.getStatus());

        if(dto.getStatus()
                == AttendanceStatus.NORMAL
        ){
            if(record.getCheckInTime()==null){

                record.setCheckInTime(
                        LocalDateTime.now()
                );

            }

        }

        record.setRemark(dto.getRemark());
        updateById(record);
    }


    //根据考勤状态统计考勤数量
    private int countStatus(List<AttendanceRecord> list, AttendanceStatus status){

        return (int) list.stream().filter(item -> status.name().equals(item.getCheckInStatus()))
                .count();

    }


    // po 2 vo
    private AttendanceVO convertVO(AttendanceRecord record){
        AttendanceVO vo = new AttendanceVO();
        BeanUtils.copyProperties(record, vo);
        return vo;
    }


}