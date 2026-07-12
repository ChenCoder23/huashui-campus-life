package com.huashui.dormitory.controller;


import com.huashui.common.response.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 学生首页（我的宿舍 + 室友信息）
 * @author
 */
@RestController
@RequestMapping("/dormitory/home")
@RequiredArgsConstructor
@Tag(name = "宿舍首页")
public class DormHomeController {



    /**
     * 我的宿舍信息
     *
     * 返回：
     * 1. 校区
     * 2. 楼栋
     * 3. 房间
     * 4. 床位
     * 5. 费用信息
     */
    @GetMapping
    @Operation(summary = "查询我的宿舍信息")
    public Result<?> getMyDormitory() {

        //使用redis
        return Result.ok();
    }


    /**
     * 我的室友列表
     *
     * 通过 Feign 查询用户姓名
     */
    @GetMapping("/roommates")
    @Operation(summary = "查询我的室友列表")
    public Result<List<?>> getRoommates() {

        // 使用redis
        return Result.ok();
    }


}
