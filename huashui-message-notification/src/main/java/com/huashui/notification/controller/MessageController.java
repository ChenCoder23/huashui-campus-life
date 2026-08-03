package com.huashui.notification.controller;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.huashui.common.response.Result;
import com.huashui.notification.domain.pojo.Message;
import com.huashui.notification.service.MessageService;
import io.swagger.v3.oas.annotations.Operation; import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController @RequestMapping("/message") @RequiredArgsConstructor @Tag(name = "消息中心")
public class MessageController {
    private final MessageService messageService;
    @GetMapping @Operation(summary = "消息列表")
    public Result<Page<Message>> list(@RequestParam(defaultValue = "1") Integer page, @RequestParam(defaultValue = "10") Integer size, @RequestParam(required = false) String type) {
        return Result.ok(messageService.page(page, size, type));
    }
    @PutMapping("/{id}/read") @Operation(summary = "标记已读")
    public Result<Void> read(@PathVariable Long id) { messageService.markRead(id); return Result.ok(); }
}