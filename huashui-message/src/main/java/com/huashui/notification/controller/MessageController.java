package com.huashui.notification.controller;

import com.huashui.common.response.Result;
import com.huashui.notification.domain.dto.MessageScrollQueryDTO;
import com.huashui.notification.domain.vo.MessageDetailVO;
import com.huashui.notification.domain.vo.MessageVO;
import com.huashui.notification.domain.vo.ScrollVO;
import com.huashui.notification.service.MessageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * 个人消息收件箱
 */
@RestController
@RequestMapping("/message")
@RequiredArgsConstructor
@Tag(name = "个人消息中心")
public class MessageController {

    private final MessageService messageService;

    @GetMapping("/scroll")
    @Operation(summary = "滚动分页查询消息")
    public Result<ScrollVO<MessageVO>> scroll(MessageScrollQueryDTO dto) {
        return Result.ok(messageService.scroll(dto));
    }

    @GetMapping("/unread/count")
    @Operation(summary = "查询未读消息数量")
    public Result<Long> unreadCount() {
        return Result.ok(messageService.unreadCount());
    }

    @GetMapping("/{id}")
    @Operation(summary = "查询消息详情")
    public Result<MessageDetailVO> detail(@PathVariable Long id) {
        return Result.ok(messageService.detail(id));
    }

    @PutMapping("/read-all")
    @Operation(summary = "全部标记已读")
    public Result<Void> readAll() {
        messageService.readAll();
        return Result.ok();
    }
}
