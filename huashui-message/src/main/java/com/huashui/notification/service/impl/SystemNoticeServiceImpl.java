package com.huashui.notification.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.huashui.common.constants.MQConstants;
import com.huashui.common.exception.BusinessException;
import com.huashui.common.response.PageResult;
import com.huashui.common.utils.UserContext;
import com.huashui.notification.Enums.NoticeStatus;
import com.huashui.notification.Enums.NoticeTopStatus;
import com.huashui.notification.conifg.noticeCoverConfig;
import com.huashui.notification.domain.dto.CreateNoticeDTO;
import com.huashui.notification.domain.dto.NoticePageQueryDTO;
import com.huashui.notification.domain.dto.NoticeScrollQueryDTO;
import com.huashui.notification.domain.dto.UpdateNoticeDTO;
import com.huashui.notification.domain.event.NoticePublishEvent;
import com.huashui.notification.domain.pojo.SystemNotice;
import com.huashui.notification.domain.vo.DictVO;
import com.huashui.notification.domain.vo.NoticeVO;
import com.huashui.notification.domain.vo.ScrollVO;
import com.huashui.notification.mapper.SystemNoticeMapper;
import com.huashui.notification.service.SystemNoticeService;
import com.huashui.notification.util.DelayMessageUtil;
import com.huashui.notification.utils.NoticeUtil;
import com.rabbitmq.client.Channel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

/**
 * 公告 Service 实现类
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class SystemNoticeServiceImpl extends ServiceImpl<SystemNoticeMapper, SystemNotice> implements SystemNoticeService {

    private static final int DEFAULT_PAGE_SIZE = 10;

    private static final int MAX_PAGE_SIZE = 50;

    private static final int LATEST_NOTICE_COUNT = 5;

    private static final int SUMMARY_LENGTH = 25;

    private final noticeCoverConfig noticeProperties;

    private final DelayMessageUtil delayMessageUtil;

    @Override
    public ScrollVO<NoticeVO> scroll(NoticeScrollQueryDTO dto) {

        int pageSize = getPageSize(dto);
        Long cursor = dto.getCursor();

        LambdaQueryWrapper<SystemNotice> wrapper = buildPublishedNoticeWrapper();

        if (cursor != null) {
            wrapper.lt(SystemNotice::getId, cursor);
        }

        wrapper.orderByDesc(SystemNotice::getId);
        wrapper.last("LIMIT " + (pageSize + 1));

        List<SystemNotice> notices = list(wrapper);

        boolean hasMore = notices.size() > pageSize;
        if (hasMore) {
            notices = notices.subList(0, pageSize);
        }

        if (notices.isEmpty()) {
            return buildEmptyScrollResult();
        }

        List<NoticeVO> voList = convertToVOList(notices);
        Long nextCursor = notices.get(notices.size() - 1).getId();

        ScrollVO<NoticeVO> result = new ScrollVO<>();
        result.setRecords(voList);
        result.setNextCursor(nextCursor);
        result.setHasMore(hasMore);
        return result;
    }

    @Override
    public List<NoticeVO> latest() {

        LambdaQueryWrapper<SystemNotice> wrapper = buildPublishedNoticeWrapper();
        wrapper.orderByDesc(SystemNotice::getId);
        wrapper.last("LIMIT " + LATEST_NOTICE_COUNT);

        return convertToVOList(list(wrapper));
    }

    @Override
    public PageResult<NoticeVO> getDraftPage(NoticePageQueryDTO dto) {

        Long userId = UserContext.getUserId();

        int pageNum = dto.getPageNum() == null || dto.getPageNum() < 1 ? 1 : dto.getPageNum();
        int pageSize = dto.getPageSize() == null || dto.getPageSize() < 1 ? 10 : Math.min(dto.getPageSize(), 50);

        LambdaQueryWrapper<SystemNotice> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SystemNotice::getPublisherId, userId);
        wrapper.eq(SystemNotice::getStatus, NoticeStatus.DRAFT);
        wrapper.like(dto.getTitle() != null && !dto.getTitle().isBlank(), SystemNotice::getTitle, dto.getTitle());
        wrapper.eq(dto.getNoticeType() != null && !dto.getNoticeType().isBlank(), SystemNotice::getNoticeType, dto.getNoticeType());
        wrapper.orderByDesc(SystemNotice::getUpdateTime);

        Page<SystemNotice> page = new Page<>(pageNum, pageSize);
        Page<SystemNotice> result = page(page, wrapper);

        List<NoticeVO> records = result.getRecords().stream().map(this::convertToVO).toList();

        PageResult<NoticeVO> pageResult = new PageResult<>();
        pageResult.setRecords(records);
        pageResult.setTotal(result.getTotal());
        pageResult.setPage(result.getPages());
        pageResult.setSize(result.getSize());
        return pageResult;
    }

    @Override
    public List<DictVO> types() {
        return List.of(
                new DictVO("停水", "WATER_STOP"),
                new DictVO("停电", "POWER_STOP"),
                new DictVO("放假", "HOLIDAY"),
                new DictVO("安检", "SAFETY_CHECK"),
                new DictVO("空调租赁", "AC_RENTAL"),
                new DictVO("其他", "OTHER")
        );
    }

    @Override
    public NoticeVO detail(Long id) {
        SystemNotice notice = getById(id);
        if (notice == null) {
            throw new BusinessException("公告不存在");
        }
        if (notice.getStatus() == NoticeStatus.DRAFT || notice.getStatus() == NoticeStatus.REVOKED) {
            throw new BusinessException("公告未发布或已撤回");
        }

        notice.setViewCount((notice.getViewCount() == null ? 0 : notice.getViewCount()) + 1);
        updateById(notice);

        return convertToVO(notice);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateNotice(Long id, UpdateNoticeDTO dto) {
        SystemNotice notice = getById(id);
        if (notice == null) {
            throw new BusinessException("公告不存在");
        }
        if (notice.getStatus() != NoticeStatus.DRAFT) {
            throw new BusinessException("仅草稿状态可修改");
        }

        notice.setTitle(dto.getTitle());
        notice.setContent(dto.getContent());
        notice.setNoticeType(dto.getNoticeType());
        notice.setAttachment(dto.getAttachment());
        notice.setPushScope(dto.getPushScope());
        notice.setTargetCampusIds(dto.getTargetCampusIds());
        notice.setTargetRoles(dto.getTargetRoles());
        notice.setTargetBuildingIds(dto.getTargetBuildingIds());
        notice.setIsTop(toTopStatus(dto.getIsTop()));
        if (dto.getPublishTime() != null) {
            notice.setPublishTime(dto.getPublishTime());
        }
        updateById(notice);

        if (dto.getPublishTime() != null) {
            NoticePublishEvent event = NoticePublishEvent.builder().noticeId(id).build();
            delayMessageUtil.sendDelayMessage(
                    MQConstants.DELAY_EXCHANGE_NOTICE,
                    MQConstants.NOTICE_DELAY_KEY,
                    event,
                    dto.getPublishTime());
        }
    }

    @Override
    public void revoke(Long id) {
        SystemNotice notice = getById(id);
        if (notice == null) {
            throw new BusinessException("公告不存在");
        }
        if (notice.getStatus() == NoticeStatus.REVOKED) {
            return;
        }
        notice.setStatus(NoticeStatus.REVOKED);
        updateById(notice);
    }

    @Override
    public void deleteDraft(Long id) {
        SystemNotice notice = getById(id);
        if (notice == null) {
            throw new BusinessException("公告不存在");
        }
        if (notice.getStatus() != NoticeStatus.DRAFT) {
            throw new BusinessException("仅草稿状态可删除");
        }
        removeById(id);
    }

    @Transactional(rollbackFor = Exception.class)
    public void publish(Long noticeId) {
        SystemNotice notice = getById(noticeId);
        if (notice == null) {
            throw new BusinessException("公告不存在");
        }
        if (notice.getStatus() != NoticeStatus.DRAFT) {
            log.info("公告当前状态不允许发布，noticeId={}, status={}", noticeId, notice.getStatus());
            return;
        }
        notice.setStatus(NoticeStatus.PUBLISHED);
        updateById(notice);
    }

    @RabbitListener(queues = MQConstants.DLX_QUEUE_NOTICE)
    public void handleNoticePublish(
            NoticePublishEvent event,
            Channel channel,
            @Header(AmqpHeaders.DELIVERY_TAG) long deliveryTag) throws IOException {

        try {
            publish(event.getNoticeId());
            channel.basicAck(deliveryTag, false);
        } catch (Exception e) {
            log.error("公告自动发布失败，noticeId={}", event.getNoticeId(), e);
            channel.basicNack(deliveryTag, false, false);
        }
    }

    @Override
    public Long create(CreateNoticeDTO dto) {

        Long publisherId = UserContext.getUserId();

        SystemNotice notice = new SystemNotice();
        notice.setTitle(dto.getTitle());
        notice.setContent(dto.getContent());
        notice.setNoticeType(dto.getNoticeType());
        notice.setAttachment(dto.getAttachment());
        notice.setPushScope(dto.getPushScope());
        notice.setTargetCampusIds(dto.getTargetCampusIds());
        notice.setTargetRoles(dto.getTargetRoles());
        notice.setTargetBuildingIds(dto.getTargetBuildingIds());
        notice.setIsTop(toTopStatus(dto.getIsTop()));
        notice.setPublishTime(dto.getPublishTime());
        notice.setPublisherId(publisherId);
        notice.setStatus(NoticeStatus.DRAFT);

        save(notice);

        NoticePublishEvent event = NoticePublishEvent.builder()
                .noticeId(notice.getId())
                .build();
        delayMessageUtil.sendDelayMessage(
                MQConstants.DELAY_EXCHANGE_NOTICE,
                MQConstants.NOTICE_DELAY_KEY,
                event,
                dto.getPublishTime());

        return notice.getId();
    }

    private LambdaQueryWrapper<SystemNotice> buildPublishedNoticeWrapper() {
        LambdaQueryWrapper<SystemNotice> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SystemNotice::getStatus, NoticeStatus.PUBLISHED);
        return wrapper;
    }

    private List<NoticeVO> convertToVOList(List<SystemNotice> notices) {
        if (notices == null || notices.isEmpty()) {
            return Collections.emptyList();
        }
        return notices.stream().map(this::convertToVO).toList();
    }

    private NoticeVO convertToVO(SystemNotice notice) {
        NoticeVO vo = BeanUtil.copyProperties(notice, NoticeVO.class);
        vo.setSummary(NoticeUtil.buildSummary(notice.getContent(), SUMMARY_LENGTH));

        if (notice.getAttachment() == null || notice.getAttachment().isBlank()) {
            vo.setCover(noticeProperties.getDefaultCover());
        } else {
            vo.setCover(notice.getAttachment());
        }
        return vo;
    }

    private ScrollVO<NoticeVO> buildEmptyScrollResult() {
        ScrollVO<NoticeVO> result = new ScrollVO<>();
        result.setRecords(Collections.emptyList());
        result.setNextCursor(null);
        result.setHasMore(false);
        return result;
    }

    private int getPageSize(NoticeScrollQueryDTO dto) {
        if (dto == null || dto.getPageSize() == null) {
            return DEFAULT_PAGE_SIZE;
        }
        return Math.min(Math.max(dto.getPageSize(), 1), MAX_PAGE_SIZE);
    }

    private NoticeTopStatus toTopStatus(Integer isTop) {
        return (isTop != null && isTop == 1) ? NoticeTopStatus.TOP : NoticeTopStatus.NOT_TOP;
    }
}
