package com.huashui.dormitory.controller;

import com.huashui.common.response.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * 住宿记录（学生入住/退宿/记录查询）
 * @author
 */
@RestController
@RequestMapping("/dormitory/record")
@RequiredArgsConstructor
@Tag(name = "住宿记录")
public class DormRecordController {



    /**
     * 住宿记录分页列表
     *
     * 支持：
     * 1. 按楼栋筛选
     * 2. 按学生筛选(学生的学号和姓名)
     * 3.按照校区
     * 4,如果已经选择了楼栋,可以指定宿舍号
     * 5.按照年份进行筛选 ,如果没有选择默认本学期
     */
    @GetMapping
    @Operation(summary = "住宿记录分页列表")
    public Result<?> pageRecord() {

        //使用redis

        return Result.ok();
    }


    /**
     * 分配床位
     *
     * 宿管操作
     */
    @PostMapping("/assign")
    @Operation(summary = "分配床位给学生")
    public Result<Void> assignBed(@RequestBody Object dto) {
        //判断该床位是否可用
        //判断学生是否存在
        // 判断学生是否已经有床位
        // 新增学生住宿记录
        return Result.ok();
    }


    /**
     * 调整住宿
     *
     * 更换房间/床位
     */
    @PostMapping("/adjust")
    @Operation(summary = "调整学生房间床位") //自动的办理之前的床位的退宿
    public Result<Void> adjustBed(@RequestBody Object dto) {
        //判断原来的床位是否存在
        //释放原来的床位
        //判断新床位是否可用
        //判断学生是否存在
        //修改原来的住宿记录
        // 新增学生住宿记录
        return Result.ok();
    }


    /**
     * 办理退宿
     */
    @PostMapping("/checkout")
    @Operation(summary = "办理学生退宿") //指定个人  年级 ,班级, 专业,楼层 ,楼栋 进行退宿,退宿舍后更新床位和房间状态
    public Result<Void> checkout(@RequestBody Object dto) {
        // 办理后更新缓存和数据库
        return Result.ok();
    }




    /**
     * 批量导入新生入住
     *
     * 通常接收 Excel 文件
     */
    @PostMapping("/import")
    @Operation(summary = "批量导入新生入住")
    public Result<Void> importRecord(@RequestParam("file") Object file) {

        return Result.ok();
    }


    /**
     * 导出住宿名单
     */
    @GetMapping("/export")
    @Operation(summary = "导出住宿名单")
    public void exportRecord() {


    }



}
