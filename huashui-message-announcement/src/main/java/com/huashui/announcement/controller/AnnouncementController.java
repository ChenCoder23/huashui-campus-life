package com.huashui.announcement.controller;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.huashui.announcement.domain.pojo.SystemNotice;
import com.huashui.announcement.service.AnnouncementService;
import com.huashui.common.response.Result;
import io.swagger.v3.oas.annotations.Operation; import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController @RequestMapping("/announcement") @RequiredArgsConstructor @Tag(name = "公告管理")
public class AnnouncementController {
    private final AnnouncementService announcementService;
    @GetMapping @Operation(summary = "公告列表")
    public Result<Page<SystemNotice>> list(@RequestParam(defaultValue = "1") Integer page, @RequestParam(defaultValue = "10") Integer size, @RequestParam(required = false) String noticeType) {
        return Result.ok(announcementService.page(page, size, noticeType));
    }
    @PostMapping @Operation(summary = "新增公告") public Result<Void> create(@RequestBody SystemNotice notice) { announcementService.save(notice); return Result.ok(); }
    @PutMapping("/{id}") @Operation(summary = "编辑公告") public Result<Void> update(@PathVariable Long id, @RequestBody SystemNotice notice) { notice.setId(id); announcementService.updateById(notice); return Result.ok(); }
    @PutMapping("/{id}/publish") @Operation(summary = "发布公告") public Result<Void> publish(@PathVariable Long id) { announcementService.publish(id); return Result.ok(); }
    @PutMapping("/{id}/revoke") @Operation(summary = "撤回公告") public Result<Void> revoke(@PathVariable Long id) { announcementService.revoke(id); return Result.ok(); }
    @DeleteMapping("/{id}") @Operation(summary = "删除公告") public Result<Void> delete(@PathVariable Long id) { announcementService.removeById(id); return Result.ok(); }
}