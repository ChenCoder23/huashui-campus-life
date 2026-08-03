package com.huashui.template.controller;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.huashui.common.response.Result;
import com.huashui.template.domain.pojo.MessageTemplate;
import com.huashui.template.service.TemplateService;
import io.swagger.v3.oas.annotations.Operation; import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController @RequestMapping("/template") @RequiredArgsConstructor @Tag(name = "消息模板管理")
public class TemplateController {
    private final TemplateService templateService;
    @GetMapping @Operation(summary = "模板列表")
    public Result<Page<MessageTemplate>> list(@RequestParam(defaultValue = "1") Integer page, @RequestParam(defaultValue = "10") Integer size) {
        return Result.ok(templateService.page(new Page<>(page, size)));
    }
    @PostMapping @Operation(summary = "新增模板") public Result<Void> create(@RequestBody MessageTemplate t) { templateService.save(t); return Result.ok(); }
    @PutMapping("/{id}") @Operation(summary = "编辑模板") public Result<Void> update(@PathVariable Long id, @RequestBody MessageTemplate t) { t.setId(id); templateService.updateById(t); return Result.ok(); }
    @DeleteMapping("/{id}") @Operation(summary = "删除模板") public Result<Void> delete(@PathVariable Long id) { templateService.removeById(id); return Result.ok(); }
}