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


}
