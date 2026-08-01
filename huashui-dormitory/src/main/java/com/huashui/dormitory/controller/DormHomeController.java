package com.huashui.dormitory.controller;

import com.huashui.common.response.Result;
import com.huashui.dormitory.domain.vo.DormHomeVO;
import com.huashui.dormitory.domain.vo.RoommateVO;
import com.huashui.dormitory.service.DormHomeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/dormitory/home")
@RequiredArgsConstructor
@Tag(name = "宿舍首页")
public class DormHomeController {

    private final DormHomeService homeService;

    @GetMapping
    @Operation(summary = "我的宿舍信息")
    public Result<DormHomeVO> myDorm() {
        return Result.ok(homeService.getMyDorm());
    }

    @GetMapping("/roommates")
    @Operation(summary = "我的室友列表")
    public Result<List<RoommateVO>> roommates() {
        return Result.ok(homeService.getMyRoommates());
    }
}
