package com.huashui.task.service.impl;


import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.huashui.api.client.dorm.BuildingClient;
import com.huashui.api.client.dorm.CampusClient;


import com.huashui.common.exception.BusinessException;
import com.huashui.common.response.PageResult;
import com.huashui.task.domain.dto.template.TaskTemplateDTO;
import com.huashui.task.domain.dto.template.TaskTemplateItemDTO;
import com.huashui.task.domain.dto.query.TaskTemplateQueryDTO;
import com.huashui.task.domain.pojo.TaskTemplate;
import com.huashui.task.domain.vo.template.CleanTaskTemplateDetailVO;
import com.huashui.task.domain.vo.template.CleanTaskTemplateVO;
import com.huashui.task.mapper.TaskTemplateMapper;
import com.huashui.task.service.TaskTemplateService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;


/**
 * 任务模板服务实现类
 */
@Service
@RequiredArgsConstructor
public class TaskTemplateServiceImpl extends ServiceImpl<TaskTemplateMapper, TaskTemplate> implements TaskTemplateService {


    private final TaskTemplateMapper taskTemplateMapper;

    private final CampusClient campusClient;

    private final BuildingClient buildingClient;





    //分页查询任务模板
    @Override
    public PageResult<CleanTaskTemplateVO> getPage(TaskTemplateQueryDTO dto) {
        // 分页对象
        Page<CleanTaskTemplateVO> page = new Page<>(dto.getPageNum(), dto.getPageSize());
        // 聚合查询模板
        Page<CleanTaskTemplateVO> result =
                taskTemplateMapper.selectTemplatePage(
                        page,
                        dto
                );
        List<CleanTaskTemplateVO> records = result.getRecords();

        if (CollUtil.isEmpty(records)) {
            return PageResult.of(
                    result.getTotal(),
                    result.getCurrent(),
                    result.getSize(),
                    records
            );
        }
        //获取所有校区ID
        Set<Long> campusIds = records.stream()
                        .map(CleanTaskTemplateVO::getCampusId)
                        .filter(Objects::nonNull)
                        .collect(Collectors.toSet());

        //获取所有楼栋ID
        Set<Long> buildingIds = records.stream()
                        .map(CleanTaskTemplateVO::getBuildingId)
                        .filter(Objects::nonNull)
                        .collect(Collectors.toSet());

        //批量查询校区名称
        Map<Long, String> campusMap = campusClient.batchName(campusIds).getData();


        //批量查询楼栋名称
        Map<Long, String> buildingMap = buildingClient.batchName(buildingIds).getData();
        //填充
        records.forEach(item -> {

            item.setCampusName(campusMap.get(item.getCampusId()));

            item.setBuildingName(buildingMap.get(item.getBuildingId()));});

        return PageResult.of(
                result.getTotal(),
                result.getCurrent(),
                result.getSize(),
                records);
    }


    //保存任务模板
    @Override
    @Transactional
    public void add(TaskTemplateDTO dto) {
        List<TaskTemplate> taskTemplates = templateDTO2PO(dto);
        saveBatch(taskTemplates);
    }

    @Override
    public void updateTaskPlateDto(TaskTemplateDTO dto) {
        // 删除原模板数据
        lambdaUpdate()
                .eq(TaskTemplate::getName, dto.getName())
                .eq(TaskTemplate::getCampusId, dto.getCampusId())
                .eq(TaskTemplate::getBuildingId, dto.getBuildingId())
                .remove();

        // 重新生成模板数据
        List<TaskTemplate> taskTemplates = templateDTO2PO(dto);

        saveBatch(taskTemplates);
    }

    @Override
    public void deleteTemplate(Long id) {
        // 查询模板明细
        TaskTemplate template =
                getById(id);

        // 删除整个模板方案
        lambdaUpdate()
                .eq(TaskTemplate::getName, template.getName())
                .eq(TaskTemplate::getCampusId, template.getCampusId())
                .eq(TaskTemplate::getBuildingId, template.getBuildingId())
                .remove();
    }



    @Override
    public CleanTaskTemplateDetailVO detail(Long id) {
        // 查询模板记录
        TaskTemplate template = getById(id);

        if (template == null) {
            throw new BusinessException("模板不存在");
        }

        // 查询该模板下所有任务明细
        List<TaskTemplate> templates = lambdaQuery()
                        .eq(TaskTemplate::getName, template.getName())
                        .eq(TaskTemplate::getCampusId, template.getCampusId())
                        .eq(TaskTemplate::getBuildingId, template.getBuildingId())
                        .list();


        // 组装详情VO
        CleanTaskTemplateDetailVO vo = new CleanTaskTemplateDetailVO();
        vo.setId(template.getId());
        vo.setName(template.getName());
        vo.setCampusId(template.getCampusId());
        vo.setBuildingId(template.getBuildingId());
        vo.setEnabled(template.getEnabled());
        vo.setCreateTime(template.getCreateTime());



       //查询校区名称
        String campusName = campusClient
                .batchName(Collections.singleton(template.getCampusId()))
                        .getData()
                        .get(template.getCampusId());
        vo.setCampusName(campusName);


        //查询楼栋名称
        String buildingName =
                buildingClient.batchName(
                        Collections.singleton(template.getBuildingId()))
            .getData()
                .get(template.getBuildingId());
        vo.setBuildingName(buildingName);
        //转换任务明细
        List<TaskTemplateItemDTO> items =
                templates.stream()
                        .map(item -> {
                            TaskTemplateItemDTO itemDTO = new TaskTemplateItemDTO();
                            itemDTO.setWorkerId(itemDTO.getWorkerId());
                            itemDTO.setWorkerName(itemDTO.getWorkerName());
                            itemDTO.setAreaDesc(itemDTO.getAreaDesc());
                            itemDTO.setTaskContent(itemDTO.getTaskContent());
                            return itemDTO;})
                        .toList();
        vo.setItems(items);
        return vo;
    }


    private List<TaskTemplate> templateDTO2PO(TaskTemplateDTO dto) {
        return dto.getItems()
                .stream()
                .map(item -> {

                    TaskTemplate template = new TaskTemplate();
                    template.setName(dto.getName());

                    template.setCampusId(dto.getCampusId());

                    template.setBuildingId(dto.getBuildingId());

                    template.setWorkerId(item.getWorkerId());

                    template.setWorkerName(item.getWorkerName());

                    template.setAreaDesc(item.getAreaDesc());

                    template.setTaskContent(item.getTaskContent());

                    template.setEnabled(1);

                    return template;

                })
                .toList();

    }
}