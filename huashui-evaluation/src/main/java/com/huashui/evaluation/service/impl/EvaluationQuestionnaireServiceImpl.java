package com.huashui.evaluation.service.impl;


import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.huashui.api.client.dorm.BuildingClient;
import com.huashui.api.client.dorm.CampusClient;
import com.huashui.common.domain.mqMessage.EvaluationEvent;
import com.huashui.common.exception.BusinessException;
import com.huashui.common.response.PageResult;
import com.huashui.common.utils.UserContext;
import com.huashui.evaluation.Enums.QuestionStatus;
import com.huashui.evaluation.Enums.ScopeType;
import com.huashui.evaluation.Enums.StudentEvaluationStatus;
import com.huashui.evaluation.domain.dto.CreateQuestionnaireDTO;
import com.huashui.evaluation.domain.dto.QuestionnaireQueryDTO;
import com.huashui.evaluation.domain.dto.UpdateQuestionnaireDTO;
import com.huashui.evaluation.domain.pojo.EvaluationQuestionItem;
import com.huashui.evaluation.domain.pojo.EvaluationQuestionnaire;
import com.huashui.evaluation.domain.pojo.EvaluationResponse;
import com.huashui.evaluation.domain.vo.QuestionItemVO;
import com.huashui.evaluation.domain.vo.QuestionnaireDetailVO;
import com.huashui.evaluation.domain.vo.QuestionnaireVO;
import com.huashui.evaluation.domain.vo.StudentQuestionnaireVO;
import com.huashui.evaluation.mapper.EvaluationQuestionnaireMapper;

import com.huashui.evaluation.service.EvaluationQuestionItemService;
import com.huashui.evaluation.service.EvaluationQuestionnaireService;
import com.huashui.evaluation.service.EvaluationResponseService;
import com.huashui.evaluation.util.DelayMessageUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.ResponseStatus;


import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class EvaluationQuestionnaireServiceImpl extends ServiceImpl<EvaluationQuestionnaireMapper, EvaluationQuestionnaire> implements EvaluationQuestionnaireService {

    private final RedisTemplate<String,Long> redisTemplate;

    private final EvaluationQuestionItemService questionItemService;

    private final DelayMessageUtil delayMessageUtil;

    private final EvaluationResponseService responseService;

    private final BuildingClient buildingClient;

    private final CampusClient campusClient;



    //管理员创建评价问卷
    @Override
    public Long create(CreateQuestionnaireDTO dto) {

        //1. 保存评价问卷
        EvaluationQuestionnaire questionnaire = BeanUtil.copyProperties(dto, EvaluationQuestionnaire.class);

        //设置创建人
        questionnaire.setCreatorId(UserContext.getUserId());

        //初始状态,默认保存为未开始
        questionnaire.setStatus(QuestionStatus.WAITING);
        save(questionnaire);

        Long questionnaireId = questionnaire.getId();

        //2. 批量保存评价问题
        List<EvaluationQuestionItem> items = dto.getItems()
                        .stream()
                        .map(itemDTO -> {
                            EvaluationQuestionItem item =
                                    BeanUtil.copyProperties(itemDTO, EvaluationQuestionItem.class);
                            item.setQuestionnaireId(questionnaireId);
                            return item;})
                        .toList();
        questionItemService.saveBatch(items);

        //发送评价问卷开始时间的消息
        delayMessageUtil.sendDelayMessage(
                "evaluation.start",
                EvaluationEvent.builder().version(1L).questionnaireId(questionnaireId).build(),
                dto.getStartTime());
        //发送评价问卷开始时间的消息
        delayMessageUtil.sendDelayMessage(
                "evaluation.end",
                EvaluationEvent.builder().version(1L).questionnaireId(questionnaireId).build(),
                dto.getEndTime());
        //向redis里保存消息版本号
        redisTemplate.opsForValue().set("evaluation:version:start:"+questionnaireId, 1L);
        redisTemplate.opsForValue().set("evaluation:version:end:"+questionnaireId, 1L);
        return questionnaireId;
    }

    @Override
    public void start(EvaluationEvent event) {
        Long questionnaireId = event.getQuestionnaireId();
        //1. 查询问卷
        EvaluationQuestionnaire questionnaire = getById(questionnaireId);
        //2. 修改状态
        questionnaire.setStatus(QuestionStatus.RUNNING);
        //3. 根据范围查询学生
        List<Long> studentIds = getStudents(questionnaire);
        // 回填需要评价的学生总人数
        int num = studentIds.size();
        questionnaire.setTotalCount(num);
        //回填状态
        updateById(questionnaire);
        //4. 保存Redis
        String key = "evaluation:waiting:" + questionnaireId;
        redisTemplate.opsForSet()
                .add(key, studentIds.toArray(new Long[0]));
        //删除redis的消息版本号
        redisTemplate.delete("evaluation:version:start:"+questionnaireId);
    }


    //获取参与评价的学生的id
    private List<Long> getStudents(EvaluationQuestionnaire questionnaire) {

        List<Long> targetIds = questionnaire.getTargetScope();
        ScopeType targetType = questionnaire.getTargetType();

        switch (targetType) {
            //指定学生
            case ScopeType.STUDENT:
                return targetIds;

            //指定校区
            case ScopeType.CAMPUS:
                return campusClient.listIdsByCampusIds(targetIds);

            //指定楼栋
            case ScopeType.BUILDING:
                return buildingClient.listStudentIdsByBuildingIds(targetIds);
            default:
                throw new BusinessException("未知评价目标类型");
        }
    }

    @Override
    public void finish(EvaluationEvent event) {
        //获取问卷的id
        Long questionnaireId = event.getQuestionnaireId();
        //1. 查询问卷
        EvaluationQuestionnaire questionnaire = getById(questionnaireId);
        //设置状态
        questionnaire.setStatus(QuestionStatus.FINISHED);
        //回填状态
        updateById(questionnaire);

        //2. 获取未评价学生
        String key = "evaluation:waiting:" + questionnaireId;
        Set<Long> studentIds = redisTemplate.opsForSet().members(key);

        if(studentIds != null && !studentIds.isEmpty()){
            //3.  todo 所有ComplateFuture异步实现 生成未评价记录
            createExpiredResponse(questionnaireId, studentIds);
        }
        //4. 删除Redis
        redisTemplate.delete(key);
        // 消息处理完毕删除redis里的消息版本号
        redisTemplate.delete("evaluation:version:end:"+questionnaireId);
    }


    //宿舍管理员分页查询评价问卷
    @Override
    public PageResult<QuestionnaireVO> getEvaluationPage(QuestionnaireQueryDTO dto) {
        //1. 获取当前管理员id
        Long userId = UserContext.getUserId();

        //2. 构造分页对象
        Page<EvaluationQuestionnaire> page =
                new Page<>(dto.getPageNum(), dto.getPageSize());

        //3. 查询
        LambdaQueryWrapper<EvaluationQuestionnaire> wrapper =
                new LambdaQueryWrapper<>();
        wrapper.eq(EvaluationQuestionnaire::getCreatorId,
                userId);
        //标题模糊查询
        wrapper.like(dto.getTitle()!=null,
                EvaluationQuestionnaire::getTitle,
                dto.getTitle());
        //状态查询
        wrapper.eq(dto.getStatus()!=null,
                EvaluationQuestionnaire::getStatus,
                dto.getStatus());
        //创建时间范围
        wrapper.ge(dto.getStartTime()!=null,
                EvaluationQuestionnaire::getCreateTime,
                dto.getStartTime());
        wrapper.le(dto.getEndTime()!=null,
                EvaluationQuestionnaire::getCreateTime,
                dto.getEndTime());
        wrapper.orderByDesc(
                EvaluationQuestionnaire::getCreateTime);


        Page<EvaluationQuestionnaire> result =
                page(page,wrapper);


        //4. 转VO
        List<QuestionnaireVO> list = BeanUtil.copyToList(result.getRecords(), QuestionnaireVO.class);

        //5. 返回
        return  PageResult.of(
                result.getTotal(),
                dto.getPageNum(),
                dto.getPageSize(),
                list);
    }


    //根据id查询评价问卷的详细信息
    @Override
    public QuestionnaireDetailVO detail(Long id) {
        //1. 查询问卷
        EvaluationQuestionnaire questionnaire = getById(id);
        if(questionnaire == null){
            throw new BusinessException("评价问卷不存在");
        }

        //2. 查询问题列表
        List<EvaluationQuestionItem> items = questionItemService.list(new LambdaQueryWrapper<EvaluationQuestionItem>()
                                .eq(EvaluationQuestionItem::getQuestionnaireId, id)
                                .orderByAsc(EvaluationQuestionItem::getSort));
        //3. VO转换
        QuestionnaireDetailVO vo =
                BeanUtil.copyProperties(
                        questionnaire,
                        QuestionnaireDetailVO.class);
        List<QuestionItemVO> questionVOList = BeanUtil.copyToList(items,
                        QuestionItemVO.class);
        vo.setQuestions(questionVOList);
        return vo;
    }





    /**
     * 创建未评价记录
     *
     * @param questionnaireId 问卷ID
     * @param studentIds 未评价学生ID
     */
    private void createExpiredResponse(Long questionnaireId, Set<Long> studentIds) {
        List<EvaluationResponse> responses =
                studentIds.stream()
                        .map(studentId -> {
                            EvaluationResponse response = new EvaluationResponse();
                            response.setQuestionnaireId(questionnaireId);
                            response.setStudentId(studentId);
                            response.setStatus(StudentEvaluationStatus.EXPIRED);
                            return response;
                        })
                        .toList();
        if(!responses.isEmpty()){
            responseService.saveBatch(responses);
            //  回填已评价的学生人数
            //查询问卷
            EvaluationQuestionnaire questionnaire = getById(questionnaireId);
            Integer totalCount = questionnaire.getTotalCount();
            int size = responses.size();
            questionnaire.setSubmitCount(totalCount - size);
            //回填
            updateById(questionnaire);
        }

    }


    //更新新评价问卷
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateEvaluation(Long id, UpdateQuestionnaireDTO dto) {

        //1. 查询问卷
        EvaluationQuestionnaire questionnaire = getById(id);
        if (questionnaire == null) {
            throw new BusinessException("评价问卷不存在");
        }

        //2. 校验状态
        if (questionnaire.getStatus() != QuestionStatus.WAITING) {

            throw new BusinessException("只有未开始的问卷可以修改");
        }

        //3. 修改基本信息
        BeanUtil.copyProperties(dto,questionnaire);
        updateById(questionnaire);

        //4. 修改问题
        if (CollUtil.isNotEmpty(dto.getQuestions())) {
            //删除旧问题
            questionItemService.remove(
                    new LambdaQueryWrapper<EvaluationQuestionItem>()
                            .eq(EvaluationQuestionItem::getQuestionnaireId, id));
            //新增新问题
            List<EvaluationQuestionItem> items =
                    dto.getQuestions()
                            .stream()
                            .map(itemDTO -> {
                                EvaluationQuestionItem item = new EvaluationQuestionItem();
                                item.setQuestionnaireId(id);
                                BeanUtil.copyProperties(itemDTO,item);
                                return item;
                            })
                            .toList();
            questionItemService.saveBatch(items);

        }
        // 判断开始时间是否改变
        if (dto.getStartTime() != null &&questionnaire.getStartTime() != dto.getStartTime()) {
            //评价开始时间改变
            Long increment = redisTemplate.opsForValue().increment("evaluation:version:start:");
            //重新发送评价问卷开始时间的消息
            delayMessageUtil.sendDelayMessage(
                    "evaluation.start",
                    EvaluationEvent.builder().version(increment).questionnaireId(id).build(),
                    dto.getStartTime());
        }
        // 判断结束时间是否改变
        if (dto.getEndTime() != null &&questionnaire.getEndTime() != dto.getEndTime()) {
            //评价结束时间改变
            Long increment = redisTemplate.opsForValue().increment("evaluation:version:end:");
            //重新发送评价问卷开始时间的消息
            delayMessageUtil.sendDelayMessage(
                    "evaluation.end",
                    EvaluationEvent.builder().version(increment).questionnaireId(id).build(),
                    dto.getStartTime());
        }

    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteEvaluation(Long id) {

        //1. 查询问卷
        EvaluationQuestionnaire questionnaire = getById(id);
        if (questionnaire == null) {
            throw new BusinessException("评价问卷不存在");
        }

        //2. 状态校验
        if (questionnaire.getStatus() != QuestionStatus.WAITING) {

            throw new BusinessException("只有未开始问卷可以删除");
        }

        //3. 删除问题
        questionItemService.remove(
                new LambdaQueryWrapper<EvaluationQuestionItem>()
                        .eq(EvaluationQuestionItem::getQuestionnaireId, id));
        //4. 删除问卷
        removeById(id);
        // 清空消息队列的消息(删除版本号信息即可)
        redisTemplate.delete("evaluation:version:start:" + id);
        redisTemplate.delete("evaluation:version:end:" + id);

    }

    @Override
    @Transactional
    public void PreFinish(Long id) {

        //1. 查询问卷
        EvaluationQuestionnaire questionnaire = getById(id);

        if(questionnaire == null){
            throw new BusinessException("评价问卷不存在");
        }

        //2. 状态判断
        if(questionnaire.getStatus() != QuestionStatus.RUNNING){
            throw new BusinessException("只有进行中的问卷可以结束");
        }


        //3. 查询未提交评价记录
        //todo  从reids里查询,然后同步数据库目前以评价学生人数,以及向数据库保存为评价的学生的记录
        List<EvaluationResponse> responses =
                responseService.list(new LambdaQueryWrapper<EvaluationResponse>()
                                .eq(EvaluationResponse::getQuestionnaireId, id)
                                .eq(EvaluationResponse::getStatus, ResponseStatus.DRAFT));
        //4. 修改未提交状态
        if(CollUtil.isNotEmpty(responses)){
            responses.forEach(response -> {
                response.setStatus(ResponseStatus.EXPIRED);});
            responseService.updateBatchById(responses);
        }

        //5. 修改问卷状态
        questionnaire.setStatus(QuestionStatus.FINISHED);
        questionnaire.setUpdateTime(LocalDateTime.now());
        updateById(questionnaire);

        //6. 删除Redis待评价集合
        redisTemplate.delete("evaluation:waiting:" + id);
        //  删除评价结束的消息版本号
        redisTemplate.delete("evaluation:version:end:" + id);
    }

    @Override
    public List<StudentQuestionnaireVO> myQuestionnaire() {

        //获取当前学生的id
        Long studentId = UserContext.getUserId();

        //1. 查询所有进行中的问卷
        List<EvaluationQuestionnaire> questionnaires =
                list(new LambdaQueryWrapper<EvaluationQuestionnaire>()
                        .eq(EvaluationQuestionnaire::getStatus, QuestionStatus.RUNNING));
        if(CollUtil.isEmpty(questionnaires)){
            return Collections.emptyList();

        }

        //2. 筛选Redis中包含当前学生的问卷
        List<EvaluationQuestionnaire> needEvaluation =
                questionnaires.stream()
                        .filter(q -> {
                            //拼装reids的key
                            String key = "evaluation:waiting:" + q.getId();
                            Boolean exists = redisTemplate.opsForSet().isMember(key, studentId);
                            return Boolean.TRUE.equals(exists);
                        })
                        .toList();

        if(CollUtil.isEmpty(needEvaluation)){
            return Collections.emptyList();

        }
        //3. 查询所有问卷的问题数量
        //获取需要评价的问卷的id
        List<Long> questionnaireIds = needEvaluation.stream()
                        .map(EvaluationQuestionnaire::getId)
                        .toList();
        Map<Long, Integer> questionCountMap =
                questionItemService.list(new LambdaQueryWrapper<EvaluationQuestionItem>()
                                        .in(EvaluationQuestionItem::getQuestionnaireId, questionnaireIds))
                        .stream()
                        .collect(Collectors.groupingBy(EvaluationQuestionItem::getQuestionnaireId,
                                Collectors.collectingAndThen(Collectors.counting(), Long::intValue)));

        //3. 转VO
        return needEvaluation.stream()
                .map(q -> {
                    StudentQuestionnaireVO vo = new StudentQuestionnaireVO();
                    BeanUtil.copyProperties(q, vo);
                    //设置问卷的问题数量
                    vo.setQuestionCount(questionCountMap.getOrDefault(q.getId(), 0));
                    return vo;})
                .toList();

    }


}