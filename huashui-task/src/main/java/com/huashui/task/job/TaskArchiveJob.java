package com.huashui.task.job;

import cn.hutool.json.JSONUtil;
import com.huashui.task.domain.pojo.Task;
import com.huashui.task.service.TaskService;
import com.xxl.job.core.handler.annotation.XxlJob;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Component
@Slf4j
public class TaskArchiveJob {


    @Resource
    private StringRedisTemplate redisTemplate;


    @Resource
    private TaskService taskService;


//每天晚上12点过5分执行
    @XxlJob("taskArchiveJob")
    public void archiveTask(){

        log.info("开始执行任务归档");


        //昨天日期
        String date = LocalDate.now()
                .minusDays(1)
                .toString();
        String key = "task:daily:" + date;

        //查询Hash所有任务
        List<Object> values =
                redisTemplate.opsForHash()
                        .values(key);


        if(CollectionUtils.isEmpty(values)){
            log.info("没有需要归档的任务");
            return;
        }
        //JSON转PO
        List<Task> tasks =
                values.stream().map(value -> JSONUtil.toBean(
                                        value.toString(),
                                        Task.class))
                        .collect(Collectors.toList());

        //批量保存mysql
        taskService.saveBatch(tasks);

        //删除redis
        redisTemplate.delete(key);

        log.info("任务归档完成,数量:{}",
                tasks.size());

    }

}