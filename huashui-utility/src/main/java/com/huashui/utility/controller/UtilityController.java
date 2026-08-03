package com.huashui.utility.controller;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.huashui.common.response.Result;
import com.huashui.utility.domain.pojo.*;
import com.huashui.utility.service.UtilityService;
import io.swagger.v3.oas.annotations.Operation; import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController @RequestMapping("/utility") @RequiredArgsConstructor @Tag(name = "水电与缴费管理")
public class UtilityController {
    private final UtilityService utilityService;

    @GetMapping("/water") @Operation(summary = "水费余额列表")
    public Result<Page<WaterBalance>> waterList(@RequestParam(defaultValue = "1") Integer page, @RequestParam(defaultValue = "10") Integer size, @RequestParam(required = false) Long buildingId) {
        return Result.ok(utilityService.waterPage(page, size, buildingId));
    }
    @GetMapping("/electric") @Operation(summary = "电费余额列表")
    public Result<Page<ElectricityBalance>> electricList(@RequestParam(defaultValue = "1") Integer page, @RequestParam(defaultValue = "10") Integer size, @RequestParam(required = false) Long buildingId) {
        return Result.ok(utilityService.electricPage(page, size, buildingId));
    }
    @GetMapping("/payment") @Operation(summary = "缴费记录")
    public Result<Page<PaymentOrder>> paymentList(@RequestParam(defaultValue = "1") Integer page, @RequestParam(defaultValue = "10") Integer size, @RequestParam(required = false) Long roomId) {
        return Result.ok(utilityService.paymentPage(page, size, roomId));
    }
}