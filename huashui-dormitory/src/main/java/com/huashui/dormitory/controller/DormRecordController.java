package com.huashui.dormitory.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 住宿记录（学生入住/退宿/记录查询）
 * @author
 */
@RestController
@RequestMapping("/dormitory/record")
@RequiredArgsConstructor
@Tag(name = "住宿记录")
public class DormRecordController {

    /*
    *
    * 1	GET	/dormitory/record	超管/宿管	住宿记录列表（分页，按楼栋/学生筛选）
2	POST	/dormitory/record/assign	宿管	分配床位给学生
3	POST	/dormitory/record/adjust	宿管	调整学生房间/床位
4	POST	/dormitory/record/checkout	宿管	办理退宿
5	GET	/dormitory/record/student/{studentId}	宿管	查某学生住宿记录
6	POST	/dormitory/record/import	宿管	批量导入新生入住
7	GET	/dormitory/record/export	宿管	导出住宿名单
    *
    *
    * */


    /**
     * 住宿记录分页列表
     *
     * 支持：
     * 1. 按楼栋筛选
     * 2. 按学生筛选
     */
    @GetMapping
    @Operation(summary = "住宿记录分页列表")
    public Result<?> pageRecord(
            @RequestParam(required = false) Long buildingId,
            @RequestParam(required = false) Long studentId,
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize) {


        return Result.success();
    }


    /**
     * 分配床位
     *
     * 宿管操作
     */
    @PostMapping("/assign")
    @Operation(summary = "分配床位给学生")
    public Result<Void> assignBed(
            @RequestBody Object dto) {


        return Result.success();
    }


    /**
     * 调整住宿
     *
     * 更换房间/床位
     */
    @PostMapping("/adjust")
    @Operation(summary = "调整学生房间床位")
    public Result<Void> adjustBed(
            @RequestBody Object dto) {


        return Result.success();
    }


    /**
     * 办理退宿
     */
    @PostMapping("/checkout")
    @Operation(summary = "办理学生退宿")
    public Result<Void> checkout(
            @RequestBody Object dto) {


        return Result.success();
    }


    /**
     * 查询学生住宿记录
     */
    @GetMapping("/student/{studentId}")
    @Operation(summary = "查询学生住宿记录")
    public Result<?> getStudentRecord(
            @PathVariable Long studentId) {


        return Result.success();
    }


    /**
     * 批量导入新生入住
     *
     * 通常接收 Excel 文件
     */
    @PostMapping("/import")
    @Operation(summary = "批量导入新生入住")
    public Result<Void> importRecord(
            @RequestParam("file") Object file) {


        return Result.success();
    }


    /**
     * 导出住宿名单
     */
    @GetMapping("/export")
    @Operation(summary = "导出住宿名单")
    public void exportRecord() {


    }



}
