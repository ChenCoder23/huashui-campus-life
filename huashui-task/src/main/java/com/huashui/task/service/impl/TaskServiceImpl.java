package com.huashui.task.service.impl;


import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.lang.UUID;
import cn.hutool.core.util.IdUtil;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.huashui.common.exception.BusinessException;
import com.huashui.common.response.PageResult;
import com.huashui.common.utils.UserContext;
import com.huashui.task.Enums.CleanTaskStatus;
import com.huashui.task.domain.dto.cleanTask.UpdateTaskStatusDTO;
import com.huashui.task.domain.dto.query.CleanTaskQueryDTO;
import com.huashui.task.domain.dto.template.TaskTemplateDTO;
import com.huashui.task.domain.dto.template.TaskTemplateItemDTO;
import com.huashui.task.domain.pojo.Task;
import com.huashui.task.domain.vo.cleanTask.CleanTaskDetailVO;
import com.huashui.task.domain.vo.cleanTask.CleanTaskVO;
import com.huashui.task.mapper.TaskMapper;
import com.huashui.task.service.TaskService;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NonNull;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class TaskServiceImpl extends ServiceImpl<TaskMapper, Task> implements TaskService {


    private final StringRedisTemplate redisTemplate;

    private final TaskMapper taskMapper;

    private final static String CLEAN_TASK_PRE = "task:daily:";

    @Override
    public void generate(TaskTemplateDTO dto) {

        List<Task> tasks = TemplateDTO2TaskPo(dto);
        String batchId = UUID.randomUUID()
                .toString();

        String key = CLEAN_TASK_PRE + LocalDate.now(); //每天日期拼接业务前缀
        //Hash存储
        tasks.forEach(task -> {
            //生成任务id
            Long taskId = IdUtil.getSnowflakeNextId();
            task.setId(taskId);
            redisTemplate.opsForHash()
                    .put(
                            key,
                            taskId.toString(),
                            JSONUtil.toJsonStr(task));});
        //24小时过期
        redisTemplate.expire(
                key,
                Duration.ofHours(24));
        // todo 把截止时间和任务执行人发送给mq延迟队列实现任务未完成提醒

    }

    //管理员分页查询保洁任务
    @Override
    public PageResult<CleanTaskVO> getCleanTaskPage(CleanTaskQueryDTO dto) {

        //获取当天日期
        LocalDate today = LocalDate.now();

        // 默认查询当天,处理开始和结束时间
        LocalDate startDate = dto.getStartDate() == null ? today : dto.getStartDate();

        LocalDate endDate = dto.getEndDate() == null ? today : dto.getEndDate();


        List<CleanTaskVO> tasks = new ArrayList<>();

        // 查询历史数据
        if(startDate.isBefore(today)){
            LocalDate mysqlEndDate = endDate.isBefore(today)
                    ? endDate
                    : today.minusDays(1);


            tasks.addAll(getHistoryTask(startDate, mysqlEndDate, dto));
        }



        // 查询当天数据
        if(!today.isBefore(startDate) && !today.isAfter(endDate)){
            tasks.addAll(getTodayTaskFromRedis(dto));

        }
        //条件过滤
        tasks = filter(tasks, dto);


        //统一分页
        return page(tasks, dto);

    }


    //从数据库查询历史的任务
    private List<CleanTaskVO> getHistoryTask(LocalDate startDate, LocalDate endDate, CleanTaskQueryDTO dto){

        //构建wrapper
        LambdaQueryWrapper<Task> wrapper = Wrappers.lambdaQuery();

        wrapper.between(Task::getTaskDate, startDate, endDate);


        wrapper.eq(dto.getWorkerId()!=null, Task::getWorkerId, dto.getWorkerId());


        wrapper.eq(dto.getCampusId()!=null, Task::getCampusId, dto.getCampusId());


        wrapper.eq(dto.getBuildingId()!=null, Task::getBuildingId, dto.getBuildingId());


        wrapper.eq(dto.getStatus()!=null, Task::getStatus, dto.getStatus());


        List<Task> list = taskMapper.selectList(wrapper);



        return list.stream()
                .map(this::convert)
                .toList();

    }
    // po2vo
    private CleanTaskVO convert(Task task){
        CleanTaskVO vo = new CleanTaskVO();

        BeanUtil.copyProperties(task, vo);

        return vo;
    }
    private List<CleanTaskVO> getTodayTaskFromRedis(CleanTaskQueryDTO dto){

        //组装redis的key
        String key = CLEAN_TASK_PRE + LocalDate.now();
        //查询rdis
        List<Object> list = redisTemplate.opsForHash().values(key);
        //处理redis的返回结果
        if(CollectionUtils.isEmpty(list)){
            return Collections.emptyList();
        }

        return list.stream()
                .map(item ->
                        BeanUtil.copyProperties(
                                item,
                                CleanTaskVO.class
                        )
                )
                .toList();

    }

    //条件过滤
    private List<CleanTaskVO> filter(List<CleanTaskVO> tasks, CleanTaskQueryDTO dto){


        return tasks.stream().filter(t ->
                        dto.getWorkerId()==null
                                || dto.getWorkerId().equals(t.getWorkerId()))


                .filter(t -> dto.getCampusId()==null
                                || dto.getCampusId().equals(t.getCampusId()))


                .filter(t ->
                        dto.getBuildingId()==null
                                || dto.getBuildingId().equals(t.getBuildingId()))


                .filter(t ->
                        dto.getStatus()==null
                                || dto.getStatus().equals(t.getStatus()))
                .toList();
    }


    //手动分页
    private PageResult<CleanTaskVO> page(List<CleanTaskVO> list, CleanTaskQueryDTO dto){

        long total = list.size();

        int start = Math.toIntExact((dto.getPageNum()-1)
                                * dto.getPageSize());

        if(start >= total){
            return PageResult.of(
                    total,
                    dto.getPageNum(),
                    dto.getPageSize(),
                    Collections.emptyList());

        }


        int end = Math.toIntExact(Math.min(start + dto.getPageSize(),
                        list.size()));


        List<CleanTaskVO> records = list.subList(start,end);

        return PageResult.of(
                total,
                dto.getPageNum(),
                dto.getPageSize(),
                records
        );

    }

    @Override
    public List<CleanTaskVO> getTodayCleanTask() {
        //1. 获取当前登录用户
        Long workerId = UserContext.getUserId();

        //2. 今日Redis Key
        String key = CLEAN_TASK_PRE + LocalDate.now();

        //3. 获取当天所有任务
        List<Object> values = redisTemplate.opsForHash().values(key);


        if(CollectionUtils.isEmpty(values)){
            return null;
        }

        //4. 转换并过滤当前保洁员任务
        return values.stream()
                .map(value -> {
                    Task task =
                            JSONUtil.toBean(
                                    value.toString(),
                                    Task.class);
                    return BeanUtil.copyProperties(
                            task,
                            CleanTaskVO.class);})
                .filter(task ->
                        workerId.equals(
                                task.getWorkerId()))
                .collect(Collectors.toList());
    }

    @Override
    // todo 待实现
    public CleanTaskDetailVO getDetail(Long id) {
        return null;
    }

    @Override
    @Transactional
    public void updateStatus(UpdateTaskStatusDTO dto) {
        //1. 获取当天任务key
        String key = CLEAN_TASK_PRE + LocalDate.now();

        //2. 根据taskId查询Redis Hash
        Task task = (Task) redisTemplate.opsForHash().get(key, dto.getTaskId().toString());

        if(task == null){
            throw new BusinessException("任务不存在");
        }


        //3. 校验状态流转
        checkStatus(task.getStatus(), dto.getStatus());

        //4. 修改状态
        task.setStatus(dto.getStatus());

        //5. 完成任务处理
        if(dto.getStatus() == CleanTaskStatus.COMPLETED){
            task.setImageUrls(dto.getImageUrls());

            if(dto.getRemark()==null || dto.getRemark().isEmpty()){

                task.setRemark(
                        task.getWorkerName()
                                +" "
                                + LocalDateTime.now()
                                +" 完成任务");

            }else{
                task.setRemark(dto.getRemark());
            }
            task.setFinishTime(LocalDateTime.now());
        }

        //6. 取消任务
        if(dto.getStatus()==CleanTaskStatus.CANCELLED){

            task.setRemark(dto.getRemark());

        }


        //7. 更新Redis Hash
        redisTemplate.opsForHash()
                .put(
                        key,
                        dto.getTaskId().toString(),
                        task);

    }

    //校验任务状态流
    private void checkStatus(CleanTaskStatus oldStatus, CleanTaskStatus newStatus){

        boolean allowed = switch (oldStatus) {

            //待执行
            case TODO -> newStatus == CleanTaskStatus.DOING
                    || newStatus == CleanTaskStatus.CANCELLED;

            //执行中
            case DOING -> newStatus == CleanTaskStatus.COMPLETED
                    || newStatus == CleanTaskStatus.CANCELLED;

            //完成后的状态不能修改
            case COMPLETED -> false;
            case CANCELLED -> false;
            default -> false;
        };
        if(!allowed){
            throw new BusinessException(
                    "非法任务状态流转:"
                            + oldStatus
                            +" -> "
                            +newStatus);

        }

    }


    //template2po
    private static @NonNull List<Task> TemplateDTO2TaskPo(TaskTemplateDTO dto) {
        //获取校区id和楼栋id
        Long campusId = dto.getCampusId();
        Long buildingId = dto.getBuildingId();
        //获取任务的详细
        List<TaskTemplateItemDTO> items = dto.getItems();
        if(CollectionUtils.isEmpty(items)){
            throw new BusinessException("任务不能为空");
        }
        //包装po

        return items.stream()
                .map(item -> {
                    Task task = new Task();
                    task.setTitle("保洁任务" +item.getAreaDesc());
                    task.setContent(item.getTaskContent());
                    task.setWorkerId(item.getWorkerId());
                    task.setWorkerName(item.getWorkerName());
                    task.setCampusId(campusId);
                    task.setBuildingId(buildingId);
                    task.setStatus(CleanTaskStatus.TODO);
                    task.setDeadline(item.getDeadline());
                    return task;
                })
                .toList();
    }
}