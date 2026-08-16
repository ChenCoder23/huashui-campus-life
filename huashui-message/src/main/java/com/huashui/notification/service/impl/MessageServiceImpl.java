package com.huashui.notification.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.huashui.common.exception.BusinessException;
import com.huashui.common.utils.UserContext;
import com.huashui.notification.Enums.MessageStatus;
import com.huashui.notification.domain.dto.MessageScrollQueryDTO;
import com.huashui.notification.domain.pojo.Message;
import com.huashui.notification.domain.vo.MessageDetailVO;
import com.huashui.notification.domain.vo.ScrollVO;
import com.huashui.notification.domain.vo.MessageVO;
import com.huashui.notification.mapper.MessageMapper;
import com.huashui.notification.service.MessageService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 消息 Service 实现
 */
@Service
public class MessageServiceImpl extends ServiceImpl<MessageMapper, Message> implements MessageService {

    //滚动分页查询消息列表
    @Override
    public ScrollVO<MessageVO> scroll(MessageScrollQueryDTO dto) {

        // 1. 获取当前用户ID
        Long userId = UserContext.getUserId();
        // 2. 处理分页数量，防止恶意传入过大的 limit
        int limit = dto.getLimit() == null ? 20 : dto.getLimit();
        limit = Math.min(Math.max(limit, 1), 50);

        // 3. 构建查询条件
        LambdaQueryWrapper<Message> wrapper = new LambdaQueryWrapper<Message>()
                .eq(Message::getReceiverId, userId)
                .orderByDesc(Message::getId)
                .last("LIMIT " + (limit + 1));

        // 4. 如果存在游标，只查询游标之前的数据
        if (dto.getCursor() != null) {
            wrapper.lt(Message::getId, dto.getCursor());
        }
        // 5. 查询消息
        List<Message> messages = list(wrapper);

        // 6. 判断是否还有下一页
        boolean hasMore = messages.size() > limit;

        if (hasMore) {
            messages = messages.subList(0, limit);
        }
        // 7. 转换为列表VO
        List<MessageVO> records = messages.stream()
                .map(message -> {
                    MessageVO vo = new MessageVO();
                    vo.setId(message.getId());
                    vo.setTitle(message.getTitle());
                    vo.setType(message.getType());
                    return vo;
                })
                .toList();
        // 8. 构造返回结果
        ScrollVO<MessageVO> result = new ScrollVO<>();
        result.setRecords(records);
        result.setHasMore(hasMore);
        // 9. 设置下一页游标
        if (!records.isEmpty()) {
            result.setNextCursor(
                    records.get(records.size() - 1).getId()
            );
        }
        return result;
    }



    //统计个人收信箱未读消息
    @Override
    public Long unreadCount() {
        // 获取当前登录用户ID
        Long userId = UserContext.getUserId();
        // 查询当前用户未读消息数量
        return count(new LambdaQueryWrapper<Message>()
                .eq(Message::getReceiverId, userId)
                .eq(Message::getStatus, MessageStatus.UNREAD));
    }

    @Override
    public MessageDetailVO detail(Long id) {
        // 1. 获取当前用户ID
        Long userId = UserContext.getUserId();

        // 2. 查询当前用户的消息
        Message message = getOne(new LambdaQueryWrapper<Message>()
                .eq(Message::getId, id)
                .eq(Message::getReceiverId, userId));

        if (message == null) {
            throw new BusinessException("消息不存在");
        }

        // 3. 如果当前消息未读，则标记为已读 todo  异步修改消息已读未读状态
        if (MessageStatus.UNREAD.equals(message.getStatus())) {
            message.setStatus(MessageStatus.READ);
            message.setReadTime(LocalDateTime.now());

            updateById(message);
        }

        // 4. 转换为详情VO
        MessageDetailVO vo = new MessageDetailVO();
        BeanUtil.copyProperties(message, vo);
        return vo;
    }


    //标记全部已读
    @Override
    public void readAll() {
        // 1. 获取当前用户ID
        Long userId = UserContext.getUserId();
        // 2. 批量更新未读消息
        Message message = new Message();
        message.setStatus(MessageStatus.READ);
        message.setReadTime(LocalDateTime.now());
        update(message, new LambdaUpdateWrapper<Message>()
                .eq(Message::getReceiverId, userId)
                .eq(Message::getStatus, MessageStatus.UNREAD));
    }
}