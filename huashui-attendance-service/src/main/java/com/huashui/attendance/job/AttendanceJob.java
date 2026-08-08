package com.huashui.attendance.job;


import cn.hutool.core.collection.CollUtil;
import cn.hutool.json.JSONUtil;
import com.huashui.api.client.system.SystemClient;
import com.huashui.api.client.user.UserClient;
import com.huashui.api.domain.vo.task.CleanerSimpleVO;
import com.huashui.attendance.domain.pojo.AttendanceRecord;
import com.huashui.attendance.enums.AttendanceStatus;
import com.huashui.attendance.service.AttendanceRecordService;
import com.huashui.common.response.Result;
import com.huashui.common.utils.SemesterUtil;
import com.xxl.job.core.handler.annotation.XxlJob;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Component;


import java.time.LocalDate;
import java.util.List;


/**
 * 保洁考勤定时任务
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class AttendanceJob {

    private final SystemClient systemClient;

    private final AttendanceRecordService attendanceService;

    private final UserClient userClient;


    /**
     * 每天凌晨00:05生成考勤记录
     *
     * cron:
     * 0 5 0 * * ?
     */
    @XxlJob("attendanceGenerateJob")
    public void generateAttendance(){
        log.info("开始生成每日保洁考勤记录");
        LocalDate today = LocalDate.now();
        //1. 判断今天是否节假日
        boolean holiday = checkHoliday(today);
        if(holiday){
            log.info("{} 是节假日，跳过自动生成考勤", today);
            return;
        }

        //2. 查询所有保洁员
        List<CleanerSimpleVO> cleaners = getCleaners();

        if(CollUtil.isEmpty(cleaners)){
            log.info("暂无保洁员");
            return;
        }

        // 2. 查询当天已经存在考勤记录的员工
        List<Long> existWorkerIds = attendanceService.lambdaQuery()
                .eq(AttendanceRecord::getAttendanceDate, today)
                .select(AttendanceRecord::getWorkerId)
                .list()
                .stream()
                .map(AttendanceRecord::getWorkerId)
                .toList();


        // 3. 过滤没有生成考勤的员工，批量构造
        List<AttendanceRecord> records = cleaners.stream()
                .filter(cleaner -> !existWorkerIds.contains(cleaner.getId()))
                .map(cleaner -> {
                    AttendanceRecord record = new AttendanceRecord();

                    record.setWorkerId(cleaner.getId());
                    record.setWorkerName(cleaner.getName());

                    record.setAttendanceDate(today);

                    // 默认未签到
                    record.setCheckInStatus(AttendanceStatus.ABSENT);

                    record.setIsHoliday(false);

                    return record;

                })
                .toList();


        // 4. 批量保存
        if (CollUtil.isNotEmpty(records)) {
            attendanceService.saveBatch(records);
        }

        log.info(
                "每日考勤生成完成 日期:{} 创建数量:{} 已存在数量:{}",
                today,
                records.size(),
                existWorkerIds.size()
        );

    }





    /**
     * 判断当天是否节假日
     *
     * 数据来源:
     *
     * sys_config
     *
     * attendance.holiday.{semester}
     *
     */
    private boolean checkHoliday(LocalDate date){

        String holidayJson = getHolidayConfig();

        if(holidayJson == null){
            return false;
        }

        List<String> holidays = JSONUtil.toList(holidayJson, String.class);

        return holidays.contains(date.toString());

    }





    /**
     * 获取节假日配置
     *
     * 后续通过Feign调用system模块
     */
    private String getHolidayConfig(){
        //组装key
        String key = "attendance.holiday." + SemesterUtil.getCurrentSemester();
        //feign调用
        Result<String> result = systemClient.getConfigValue(key);
        return result.getData();

    }




    /**
     * 查询保洁员
     *
     * 通过Feign调用user模块
     */
    private List<CleanerSimpleVO> getCleaners(){
        Result<List<CleanerSimpleVO>> result = userClient.listByRole("CLEANER");
        return result.getData();
        }



    }





