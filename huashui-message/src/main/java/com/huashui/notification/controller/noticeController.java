package com.huashui.notification.controller;

import com.huashui.common.response.Result;
import com.huashui.notification.domain.dto.NoticeScrollQueryDTO;
import com.huashui.notification.domain.vo.NoticeVO;
import com.huashui.notification.domain.vo.ScrollVO;
import com.huashui.notification.service.SystemNoticeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 校园公告（前台）
 */
@RestController
@RequestMapping("/notice")
@RequiredArgsConstructor
@Tag(name = "校园公告中心中心")
public class noticeController {

    private final SystemNoticeService noticeService;

    @GetMapping("/scroll")
    @Operation(summary = "滚动分页查询公告")
    public Result<ScrollVO<NoticeVO>> scroll(NoticeScrollQueryDTO dto) {
        return Result.ok(noticeService.scroll(dto));
    }

    @GetMapping("/latest")
    @Operation(summary = "查询最近的五条公告")
    public Result<List<NoticeVO>> latest() {
        return Result.ok(noticeService.latest());
    }

    @GetMapping("/{id}")
    @Operation(summary = "查询公告详情")
    public Result<NoticeVO> detail(@PathVariable Long id) {
        return Result.ok(noticeService.detail(id));
    }
}
