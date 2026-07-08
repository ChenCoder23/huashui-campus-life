package com.huashui.dormitory.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 学生首页（我的宿舍 + 室友信息）
 * @author
 */
@RestController
@RequestMapping("/dormitory/home")
@RequiredArgsConstructor
@Tag(name = "宿舍首页")
public class DormHomeController {

    /*
    *
    *
    * #	方法	路径	权限	说明
1	GET	/dormitory/home	学生	我的宿舍信息（校区/楼栋/房间/床位/费用）
2	GET	/dormitory/home/roommates	学生	我的室友列表（Feign 查姓名）*/



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


        return Result.success();
    }


    /**
     * 我的室友列表
     *
     * 通过 Feign 查询用户姓名
     */
    @GetMapping("/roommates")
    @Operation(summary = "查询我的室友列表")
    public Result<List<?>> getRoommates() {


        return Result.success();
    }


}
