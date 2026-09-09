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

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;


import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;
import java.util.concurrent.Executor;
import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicInteger;


/**
 * 保洁考勤定时任务
 */
@Slf4j
@Component
public class AttendanceJob {

    private final SystemClient systemClient;

    private final AttendanceRecordService attendanceService;

    private final UserClient userClient;

    private final Executor attendanceExecutor;

    @Value("${attendance.generate.batch-size:500}")
    private int batchSize;

    @Value("${attendance.generate.parallelism:4}")
    private int parallelism;

    public AttendanceJob(
            SystemClient systemClient,
            AttendanceRecordService attendanceService,
            UserClient userClient,
            @Qualifier("ioExecutor") Executor attendanceExecutor) {
        this.systemClient = systemClient;
        this.attendanceService = attendanceService;
        this.userClient = userClient;
        this.attendanceExecutor = attendanceExecutor;
    }

    /**
     * 每天凌晨00:05生成考勤记录
     *
     * cron:
     * 0 5 0 * * ?
     */
    @XxlJob("attendanceGenerateJob")
    public void generateAttendance(){
        log.info(">>>>>>>>>>开始生成每日保洁考勤记录");
        long startTime = System.currentTimeMillis();
        LocalDate today = LocalDate.now();
        //1. 判断今天是否节假日
       /* boolean holiday = checkHoliday(today);
        if(holiday){
            log.info("{} 是节假日，跳过自动生成考勤", today);
            return;
        }
*/
        //2. 查询所有保洁员
        List<CleanerSimpleVO> cleaners = getCleaners();

        if(CollUtil.isEmpty(cleaners)){
            log.info("暂无保洁员");
            return;
        }

        // 3. 只查询当天已生成考勤的员工ID
        List<Long> existWorkerIds = attendanceService.getExistingWorkerIds(today);
        Set<Long> pendingWorkerIds = new HashSet<>(existWorkerIds);

        // 4. 使用HashSet过滤未生成记录的员工，同时防止输入中的重复员工
        List<AttendanceRecord> records = new ArrayList<>();
        for (CleanerSimpleVO cleaner : cleaners) {
            if (cleaner.getId() == null || !pendingWorkerIds.add(cleaner.getId())) {
                continue;
            }

            records.add(AttendanceRecord.builder()
                    .workerId(cleaner.getId())
                    .workerName(cleaner.getRealName())
                    .attendanceDate(today)
                    .checkInStatus(AttendanceStatus.ABSENT)
                    .isHoliday(false)
                    .build());
        }

        if (CollUtil.isNotEmpty(records)) {
            int savedCount = saveSharded(records);
            log.info("每日考勤生成完成 日期:{} 创建数量:{} 已存在数量:{} 耗时:{}ms",
                    today, savedCount, existWorkerIds.size(), System.currentTimeMillis() - startTime);
            return;
        }

        log.info("每日考勤无新增记录 日期:{} 已存在数量:{}", today, existWorkerIds.size());
    }


    /**
     * 将待保存记录拆分成固定大小的分片，并通过IO线程池并行写入。
     *
     * 每个分片独立提交，避免一条超大事务或一次超长批量写入占用数据库太久。
     */
    private int saveSharded(List<AttendanceRecord> records) {
        int safeBatchSize = Math.max(1, batchSize);
        int safeParallelism = Math.max(1, parallelism);
        List<List<AttendanceRecord>> shards = CollUtil.split(records, safeBatchSize);
        Semaphore permits = new Semaphore(safeParallelism);
        AtomicInteger savedCount = new AtomicInteger();

        try {
            CompletableFuture.allOf(shards.stream()
                    .map(shard -> CompletableFuture.runAsync(() -> {
                        try {
                            permits.acquire();
                        } catch (InterruptedException e) {
                            Thread.currentThread().interrupt();
                            throw new IllegalStateException("考勤记录保存被中断", e);
                        }
                        try {
                            attendanceService.saveBatch(shard);
                            savedCount.addAndGet(shard.size());
                        } finally {
                            permits.release();
                        }
                    }, attendanceExecutor))
                    .toArray(CompletableFuture[]::new)).join();
        } catch (CompletionException e) {
            Throwable cause = e.getCause();
            log.error("分片保存考勤记录失败 批次大小:{} 并行度:{}", safeBatchSize, safeParallelism, cause);
            if (cause instanceof RuntimeException runtimeException) {
                throw runtimeException;
            }
            if (cause instanceof Error error) {
                throw error;
            }
            throw new IllegalStateException("分片保存考勤记录失败", cause);
        }

        return savedCount.get();
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
    private List<CleanerSimpleVO> getCleaners() {
        try {
            log.info(" 开始调用 userClient.listByRole ");
            Result<List<CleanerSimpleVO>> result =
                    userClient.listByRole("CLEANER");
            log.info(" Feign调用完成 ");
            log.info("result = {}", result);
            return result.getData();
        } catch (Exception e) {
            log.error(" Feign调用异常", e);
            throw e;
        }
    }



    }





