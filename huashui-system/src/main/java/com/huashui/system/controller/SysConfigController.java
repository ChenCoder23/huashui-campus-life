package com.huashui.system.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.huashui.common.response.Result;
import com.huashui.system.domain.dto.ConfigDTO;
import com.huashui.system.domain.pojo.SysConfig;
import com.huashui.system.service.SysConfigService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/system/config")
@RequiredArgsConstructor
@Tag(name = "系统配置管理")
public class SysConfigController {

    private final SysConfigService configService;

    @GetMapping
    @Operation(summary = "系统配置列表")
    public Result<Page<SysConfig>> list(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(required = false) String configGroup) {
        return Result.ok(configService.page(page, size, configGroup));
    }

    @PostMapping
    @Operation(summary = "新增系统配置")
    public Result<Void> create(@Valid @RequestBody ConfigDTO dto) {
        configService.create(dto);
        return Result.ok();
    }

    @PutMapping("/{id}")
    @Operation(summary = "编辑系统配置")
    public Result<Void> update(@PathVariable Long id, @Valid @RequestBody ConfigDTO dto) {
        configService.update(id, dto);
        return Result.ok();
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "删除系统配置")
    public Result<Void> delete(@PathVariable Long id) {
        configService.removeById(id);
        return Result.ok();
    }
}