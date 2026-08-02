package com.huashui.system.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.huashui.common.response.Result;
import com.huashui.system.domain.dto.DictDataDTO;
import com.huashui.system.domain.pojo.SysDictData;
import com.huashui.system.service.SysDictDataService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Tag(name = "字典数据管理")
public class DictDataController {

    private final SysDictDataService dictDataService;

    @GetMapping("/system/dict-data")
    @Operation(summary = "字典数据列表")
    public Result<Page<SysDictData>> list(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(required = false) String dictType) {
        return Result.ok(dictDataService.page(page, size, dictType));
    }

    @PostMapping("/system/dict-data")
    @Operation(summary = "新增字典数据")
    public Result<Void> create(@Valid @RequestBody DictDataDTO dto) {
        dictDataService.create(dto);
        return Result.ok();
    }

    @PutMapping("/system/dict-data/{id}")
    @Operation(summary = "编辑字典数据")
    public Result<Void> update(@PathVariable Long id, @Valid @RequestBody DictDataDTO dto) {
        dictDataService.update(id, dto);
        return Result.ok();
    }

    @DeleteMapping("/system/dict-data/{id}")
    @Operation(summary = "删除字典数据")
    public Result<Void> delete(@PathVariable Long id) {
        dictDataService.removeById(id);
        return Result.ok();
    }

    @GetMapping("/dict/data/{dictType}")
    @Operation(summary = "根据类型查询字典数据（公共接口）")
    public Result<List<SysDictData>> getByType(@PathVariable String dictType) {
        return Result.ok(dictDataService.getByDictType(dictType));
    }
}