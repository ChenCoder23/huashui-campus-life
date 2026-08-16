package com.huashui.dormitory.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.huashui.common.response.PageResult;
import com.huashui.common.response.Result;
import com.huashui.dormitory.domain.dto.CampusDTO;
import com.huashui.dormitory.domain.pojo.SysCampus;
import com.huashui.dormitory.service.SysCampusService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/dormitory/campus")
@RequiredArgsConstructor
@Tag(name = "校区管理")
public class CampusController {

    private final SysCampusService campusService;

    @GetMapping
    @Operation(summary = "校区列表（分页）")
    public PageResult<SysCampus> list(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size) {
        return campusService.page(page, size);
    }

    @PostMapping
    @Operation(summary = "新增校区")
    public Result<Void> create(@Valid @RequestBody CampusDTO dto) {
        campusService.create(dto);
        return Result.ok();
    }

    @PutMapping("/{id}")
    @Operation(summary = "编辑校区")
    public Result<Void> update(@PathVariable Long id, @Valid @RequestBody CampusDTO dto) {
        campusService.update(id, dto);
        return Result.ok();
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "删除校区")
    public Result<Void> delete(@PathVariable Long id) {
        campusService.deleteById(id);
        return Result.ok();
    }

    @GetMapping("/options")
    @Operation(summary = "校区下拉选项")
    public Result<List<SysCampus>> options() {
        return Result.ok(campusService.listEnabled());
    }
}
