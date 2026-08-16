package com.huashui.notification.controller;

import com.huashui.common.response.PageResult;
import com.huashui.common.response.Result;
import com.huashui.notification.domain.dto.CreateNoticeDTO;
import com.huashui.notification.domain.dto.NoticePageQueryDTO;
import com.huashui.notification.domain.dto.UpdateNoticeDTO;
import com.huashui.notification.domain.vo.DictVO;
import com.huashui.notification.domain.vo.NoticeVO;
import com.huashui.notification.service.SystemNoticeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 校园公告管理
 */
@RestController
@RequestMapping("/notice/admin")
@RequiredArgsConstructor
@Tag(name = "校园公告中心中心")
public class adminNoticeController {

    private final SystemNoticeService noticeService;

    @GetMapping("/draft")
    @Operation(summary = "查询我的公告草稿")
    public PageResult<NoticeVO> draft(NoticePageQueryDTO dto) {
        return noticeService.getDraftPage(dto);
    }

    @GetMapping("/types")
    @Operation(summary = "查询所有公告类型")
    public Result<List<DictVO>> types() {
        return Result.ok(noticeService.types());
    }

    @PostMapping
    @Operation(summary = "创建公告")
    public Result<Long> create(@RequestBody CreateNoticeDTO dto) {
        return Result.ok(noticeService.create(dto));
    }

    @PutMapping("/{id}")
    @Operation(summary = "修改公告")
    public Result<Void> update(@PathVariable Long id, @RequestBody UpdateNoticeDTO dto) {
        noticeService.updateNotice(id, dto);
        return Result.ok();
    }

    @PutMapping("/{id}/revoke")
    @Operation(summary = "撤回公告")
    public Result<Void> revoke(@PathVariable Long id) {
        noticeService.revoke(id);
        return Result.ok();
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "删除草稿")
    public Result<Void> delete(@PathVariable Long id) {
        noticeService.deleteDraft(id);
        return Result.ok();
    }
}
