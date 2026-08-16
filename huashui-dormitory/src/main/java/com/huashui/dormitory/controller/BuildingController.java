package com.huashui.dormitory.controller;


import com.huashui.common.response.PageResult;
import com.huashui.common.response.Result;
import com.huashui.dormitory.domain.dto.BuildingConfigDTO;
import com.huashui.dormitory.domain.dto.BuildingCreateDTO;
import com.huashui.dormitory.domain.dto.BuildingPageDTO;
import com.huashui.dormitory.domain.dto.BuildingUpdateDTO;
import com.huashui.dormitory.domain.pojo.DormBuilding;
import com.huashui.dormitory.domain.vo.BuildingDetailVO;
import com.huashui.dormitory.domain.vo.BuildingPageVO;
import com.huashui.dormitory.service.DormBuildingService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/dormitory/building")
@RequiredArgsConstructor
@Tag(name = "楼栋管理")
public class BuildingController {

    private final DormBuildingService buildingService;

    @GetMapping
    @Operation(summary = "楼栋列表（分页）")
    public PageResult<BuildingPageVO> list(BuildingPageDTO dto) {
        return buildingService.getBuildingsPage(dto);
    }

    @PostMapping
    @Operation(summary = "新增楼栋（含硬件配置）")
    public Result<Void> create(@Valid @RequestBody BuildingCreateDTO dto) {
        buildingService.create(dto);
        return Result.ok();
    }

    @PutMapping("/{id}")
    @Operation(summary = "编辑楼栋")
    public Result<Void> update(@PathVariable Long id, @Valid @RequestBody BuildingUpdateDTO dto) {
        buildingService.update(id, dto);
        return Result.ok();
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "删除楼栋")
    public Result<Void> delete(@PathVariable Long id) {
        buildingService.deleteById(id);
        return Result.ok();
    }

    @GetMapping("/{id}")
    @Operation(summary = "楼栋详情（含硬件配置）")
    public Result<BuildingDetailVO> detail(@PathVariable Long id) {
        return Result.ok(buildingService.getDetail(id));
    }

    @PutMapping("/{id}/config")
    @Operation(summary = "更新楼栋硬件配置")
    public Result<Void> updateConfig(@PathVariable Long id, @Valid @RequestBody BuildingConfigDTO dto) {
        buildingService.updateConfig(id, dto);
        return Result.ok();
    }

    @GetMapping("/options")
    @Operation(summary = "楼栋下拉选项")
    public Result<List<DormBuilding>> options(@RequestParam(required = false) Long campusId) {
        return Result.ok(buildingService.listEnabledByCampus(campusId));
    }
}
