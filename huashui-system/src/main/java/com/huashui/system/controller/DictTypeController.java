package com.huashui.system.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.huashui.common.response.Result;
import com.huashui.system.domain.dto.DictTypeDTO;
import com.huashui.system.domain.pojo.SysDictType;
import com.huashui.system.service.SysDictTypeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/system/dict-type")
@RequiredArgsConstructor
@Tag(name = "字典类型管理")
public class DictTypeController {

    private final SysDictTypeService dictTypeService;

    @GetMapping
    @Operation(summary = "字典类型列表")
    public Result<Page<SysDictType>> list(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(required = false) String keyword) {
        return Result.ok(dictTypeService.page(page, size, keyword));
    }

    @PostMapping
    @Operation(summary = "新增字典类型")
    public Result<Void> create(@Valid @RequestBody DictTypeDTO dto) {
        dictTypeService.create(dto);
        return Result.ok();
    }

    @PutMapping("/{id}")
    @Operation(summary = "编辑字典类型")
    public Result<Void> update(@PathVariable Long id, @Valid @RequestBody DictTypeDTO dto) {
        dictTypeService.update(id, dto);
        return Result.ok();
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "删除字典类型")
    public Result<Void> delete(@PathVariable Long id) {
        dictTypeService.removeById(id);
        return Result.ok();
    }
}