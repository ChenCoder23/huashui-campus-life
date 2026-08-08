package com.huashui.task.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.huashui.api.client.dorm.BuildingClient;
import com.huashui.api.client.dorm.RoomClient;
import com.huashui.api.client.user.UserClient;
import com.huashui.api.domain.vo.dorm.room.RoomDetailVO;
import com.huashui.api.domain.vo.dorm.room.RoomVO;
import com.huashui.common.constants.MQConstants;
import com.huashui.common.domain.dto.UserSimpleInfo;

import com.huashui.common.domain.mqMessage.RepairEvent;
import com.huashui.common.exception.BusinessException;
import com.huashui.common.response.PageResult;
import com.huashui.common.response.Result;
import com.huashui.common.utils.UserContext;

import com.huashui.task.Enums.RepairEventType;
import com.huashui.task.Enums.RepairTaskStatus;
import com.huashui.task.domain.dto.query.RepairQueryDTO;
import com.huashui.task.domain.dto.repair.RepairAssignDTO;
import com.huashui.task.domain.dto.repair.RepairCompleteDTO;
import com.huashui.task.domain.dto.repair.RepairCreateDTO;
import com.huashui.task.domain.pojo.RepairOrder;
import com.huashui.task.domain.vo.repair.RepairVO;
import com.huashui.task.domain.vo.repair.repairDetailVO;
import com.huashui.task.mapper.RepairOrderMapper;
import com.huashui.task.service.RepairOrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class RepairOrderServiceImpl extends ServiceImpl<RepairOrderMapper, RepairOrder> implements RepairOrderService {

    private final UserClient userClient;

    private final RoomClient roomClient;

    private final RabbitTemplate rabbitTemplate;

    private final RepairOrderMapper repairOrderMapper;

    private final BuildingClient buildingClient;



    @Override
    @Transactional
    public void createRepairOrder(RepairCreateDTO dto) {
        //1. 获取当前用户
        Long studentId = UserContext.getUserId();

        //2. 查询学生信息
        UserSimpleInfo user = userClient.getUserInfoById(studentId);

        //3. 查询宿舍信息
        RoomVO room = roomClient.getRoomInfoById(dto.getRoomId());
        //4. 创建订单
        RepairOrder order = new RepairOrder();
        order.setOrderNo(generateOrderNo());
        order.setStudentId(studentId);
        order.setStudentName(user.getRealName());
        order.setCampusId(room.getCampusId());
        order.setBuildingId(room.getBuildingId());
        order.setRoomId(room.getId());
        order.setRepairType(dto.getRepairType());
        order.setDescription(dto.getDescription());
        order.setImages(JSONUtil.toJsonStr(dto.getImages()));
        order.setContactPhone(dto.getContactPhone());
        order.setAppointmentTime(dto.getAppointmentTime());
        order.setStatus(RepairTaskStatus.PENDING);
        save(order);
        //5. 发送报修消息事件
        RepairEvent event =
                RepairEvent.builder()
                        .repairId(order.getId())
                        .orderNo(order.getOrderNo())
                        .studentId(order.getStudentId())
                        .buildingId(order.getBuildingId())
                        .roomId(order.getRoomId())
                        .repairType(order.getRepairType())
                        .description(
                                RepairEvent.formatDescription(
                                        order.getDescription()))
                        .eventType(RepairEventType.CREATE.name())
                        .build();
        rabbitTemplate.convertAndSend(
                MQConstants.TOPIC_EXCHANGE,
                MQConstants.REPAIR_CREATED_KEY,
                event);
    }

    //学生分页查询我的报修记录
    @Override
    public PageResult<RepairVO> getMyRepairPage(RepairQueryDTO dto) {

        //公共分页处理方法
        LambdaQueryWrapper<RepairOrder> wrapper =
                buildQueryWrapper(dto);

        //限定学生id
        wrapper.eq(
                RepairOrder::getStudentId,
                UserContext.getUserId());

        return pageQuery(wrapper,dto);

    }

    @Override
    public Result<repairDetailVO> getDetailByOrderId(Long id) {
        //获取当前用户id
        Long studentId = UserContext.getUserId();
        RepairOrder order = getById(id);
        if(order == null){
            throw new BusinessException("报修记录不存在");
        }
        if(!order.getStudentId().equals(studentId)){
            throw new BusinessException("无权查看该报修记录");
        }


        repairDetailVO vo = new repairDetailVO();


        BeanUtils.copyProperties(order, vo);

        //图片转换
        vo.setImages(JSONUtil.toList(
                        order.getImages(),
                        String.class));


        vo.setRepairImages(JSONUtil.toList(
                        order.getRepairImages(),
                        String.class));

        //查询宿舍完整信息
        RoomDetailVO room = roomClient.getRoomDetailInfoById(order.getRoomId());


        vo.setCampusName(room.getCampusName());
        vo.setBuildingName(room.getBuildingName());

        return Result.ok(vo);
    }

    @Override
    public void cancelOrder(Long id) {
        //1. 获取当前学生
        Long studentId = UserContext.getUserId();

        //2. 查询报修订单
        RepairOrder order = getById(id);
        if(order == null){
            throw new BusinessException("报修记录不存在");
        }

        //3. 校验是否本人报修
        if(!order.getStudentId().equals(studentId)){
            throw new BusinessException("无权取消该报修");
        }

        //4. 校验状态
        if(!RepairTaskStatus.PENDING.equals(order.getStatus())){
            throw new BusinessException(
                    "当前状态无法取消报修"
            );
        }

        //5. 修改状态
        order.setStatus(RepairTaskStatus.CANCELLED);

        updateById(order);

        //5. 发送报修取消消息事件
        RepairEvent event =
                RepairEvent.builder()
                        .repairId(order.getId())
                        .orderNo(order.getOrderNo())
                        .studentId(order.getStudentId())
                        .buildingId(order.getBuildingId())
                        .roomId(order.getRoomId())
                        .repairType(order.getRepairType())
                        .description(
                                RepairEvent.formatDescription(
                                        order.getDescription()
                                )
                        )
                        .eventType(
                                RepairEventType.CANCEL.name()
                        )
                        .build();
        rabbitTemplate.convertAndSend(
                MQConstants.TOPIC_EXCHANGE,
                MQConstants.REPAIR_CANCEL_KEY,
                event);
    }


    //宿舍管理员分页查询负责楼栋的报修记录
    @Override
    public PageResult<RepairVO> adminPage(RepairQueryDTO dto) {
        LambdaQueryWrapper<RepairOrder> wrapper =
                buildQueryWrapper(dto);
        //获取宿舍管理员负责的楼栋id
        Long buildingId = getCurrentManagerBuildingId(UserContext.getUserId());
        wrapper.eq(
                RepairOrder::getBuildingId,
                buildingId);
        return pageQuery(wrapper,dto);

    }

    //获取当前宿舍管理员负责的楼栋id todo 使用feign远程调用
    private Long getCurrentManagerBuildingId(Long userId) {
        return null;
    }

    @Override
    public PageResult<RepairVO> workerRepairPage(RepairQueryDTO dto) {
        //添加公共分页参数
        LambdaQueryWrapper<RepairOrder> wrapper =
                buildQueryWrapper(dto);

        //添加限定维修工的id
        wrapper.eq(
                RepairOrder::getRepairerId,
                UserContext.getUserId());
        return pageQuery(wrapper,dto);
    }

    //宿舍管理员对报修申请进行派单
    @Override
    public void assign(RepairAssignDTO dto) {
        //1. 获取当前宿管
        Long managerId = UserContext.getUserId();

        //2. 查询报修订单
        RepairOrder order = getById(dto.getRepairId());

        if(order == null){
            throw new BusinessException("报修工单不存在");

        }

        //4. 状态校验
        if(!RepairTaskStatus.PENDING.equals(order.getStatus())){
            throw new BusinessException("当前状态不能派单");

        }

        //6. 更新订单
        order.setRepairerId(dto.getRepairerId());
        order.setRepairerName(dto.getRepairerName());
        order.setAssignerId(managerId);
        order.setAssignedTime(LocalDateTime.now());
        order.setStatus(RepairTaskStatus.ASSIGNED);
        updateById(order);

        //MQ通知被分配的worker
        RepairEvent event = RepairEvent.builder()
                        .repairId(order.getId())
                        .orderNo(order.getOrderNo())
                        .studentId(order.getStudentId())
                        .buildingId(order.getBuildingId())
                        .roomId(order.getRoomId())
                        .repairerId(order.getRepairerId())
                        .repairType(order.getRepairType())
                        .description(
                                RepairEvent.formatDescription(
                                        order.getDescription()))
                        .eventType(RepairEventType.ASSIGN.name())
                        .build();
        rabbitTemplate.convertAndSend(
                MQConstants.TOPIC_EXCHANGE,
                MQConstants.REPAIR_ASSIGNED_KEY,
                event
        );

    }

    //维修工开始执行任务
    @Override
    public void start(Long id) {
        //1. 获取当前维修工
        Long repairerId = UserContext.getUserId();

        //2. 查询工单
        RepairOrder order = getById(id);

        if(order == null){
            throw new BusinessException("维修工单不存在");
        }

        //3. 校验是否自己的任务
        if(!repairerId.equals(order.getRepairerId())){

            throw new BusinessException(
                    "无权处理该维修任务");

        }


        //4. 状态校验
        if(!RepairTaskStatus.ASSIGNED.equals(order.getStatus())){
            throw new BusinessException(
                    "当前状态不能开始维修");
        }


        //5. 更新状态
        order.setStatus(RepairTaskStatus.REPAIRING);
        order.setRepairTime(LocalDateTime.now());
        updateById(order);
    }


    //维修工确认完成任务
    @Override
    public void complete(RepairCompleteDTO dto) {
        //1. 获取当前维修工
        Long repairerId = UserContext.getUserId();

        //2. 查询工单
        RepairOrder order = getById(dto.getRepairId());

        if(order == null){
            throw new BusinessException(
                    "维修工单不存在");

        }


        //3. 校验是否自己的任务
        if(!repairerId.equals(order.getRepairerId())){
            throw new BusinessException(
                    "无权处理该维修任务");

        }


        //4. 状态校验

        if(!RepairTaskStatus.REPAIRING.equals(order.getStatus())){
            throw new BusinessException(
                    "当前状态不能完成维修"
            );

        }

        //5. 更新维修结果

        String result = dto.getRepairResult();

        if(StrUtil.isBlank(result)){
            result = buildDefaultRepairResult(order);
        }

        order.setRepairResult(result);

        //6. 保存维修图片
        if(dto.getRepairImages()!=null){
            order.setRepairImages(JSONUtil.toJsonStr(
                            dto.getRepairImages()));
        }


        //7. 更新时间
        order.setRepairTime(LocalDateTime.now());

        //8. 修改状态
        order.setStatus(RepairTaskStatus.COMPLETED);
        updateById(order);

        // todo
        //发送消息到信息微服务
        RepairEvent repairEvent = RepairEvent.builder()
                        .repairId(order.getId())
                        .orderNo(order.getOrderNo())
                        .studentId(order.getStudentId())
                        .buildingId(order.getBuildingId())
                        .roomId(order.getRoomId())
                        .repairerId(order.getRepairerId())
                        .repairType(order.getRepairType())
                        .description(RepairEvent.formatDescription(
                                        order.getDescription()))
                        .eventType(RepairEventType.COMPLETE.name())
                        .build();
        rabbitTemplate.convertAndSend(
                MQConstants.TOPIC_EXCHANGE,
                MQConstants.REPAIR_COMPLETED_MESSAGE_KEY,
                repairEvent);
        //发送消息到评价微服务
        RepairEvent completedEvent = RepairEvent.builder()
                        .repairId(order.getId())
                        .orderNo(order.getOrderNo())
                        .studentId(order.getStudentId())
                        .repairerId(order.getRepairerId())
                        .buildingId(order.getBuildingId())
                        .roomId(order.getRoomId())
                        .completeTime(LocalDateTime.now())
                        .build();
        rabbitTemplate.convertAndSend(
                MQConstants.TOPIC_EXCHANGE,
                MQConstants.REPAIR_COMPLETED_EVALUATION_KEY,
                completedEvent);
        // todo  修改为发一次消息剩下的两个同时监听

    }

    //工单号的生成
    private String generateOrderNo(){
        return "BX"
                + LocalDateTime.now()
                .format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"))
                + RandomUtil.randomNumbers(4);

    }

    //生成默认的维修工维修结果
    private String buildDefaultRepairResult(RepairOrder order){

        //查询房间
        RoomDetailVO room = roomClient.getRoomDetailInfoById(order.getRoomId());


        String roomNumber =
                room == null ?
                        "" :
                        room.getRoomNumber();

        return String.format(
                "%s已完成%s房间报修处理：%s",
                order.getRepairerName(),
                roomNumber,
                order.getDescription()
        );

    }

    //构建分页查询的wrapper
    private LambdaQueryWrapper<RepairOrder> buildQueryWrapper(RepairQueryDTO dto){

        LambdaQueryWrapper<RepairOrder> wrapper = Wrappers.lambdaQuery();

        wrapper.eq(
                dto.getStatus()!=null,
                RepairOrder::getStatus,
                dto.getStatus());
        wrapper.eq(
                dto.getRepairType()!=null,
                RepairOrder::getRepairType,
                dto.getRepairType());
        wrapper.ge(
                dto.getStartDate()!=null,
                RepairOrder::getCreateTime,
                dto.getStartDate());
        wrapper.le(
                dto.getEndDate()!=null,
                RepairOrder::getCreateTime,
                dto.getEndDate());
        wrapper.orderByDesc(
                RepairOrder::getCreateTime);
        return wrapper;

    }

    //分页处理
    private PageResult<RepairVO> pageQuery(LambdaQueryWrapper<RepairOrder> wrapper, RepairQueryDTO dto){
        Page<RepairOrder> page =
                new Page<>(
                        dto.getPageNum(),
                        dto.getPageSize());

        Page<RepairOrder> result =
                repairOrderMapper.selectPage(
                        page,
                        wrapper);
        List<RepairVO> records =
                BeanUtil.copyToList(
                        result.getRecords(),
                        RepairVO.class);
        return PageResult.of(
                result.getTotal(),
                dto.getPageNum(),
                dto.getPageSize(),
                records);

    }
}