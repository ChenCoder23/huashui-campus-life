package com.huashui.dormitory.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 楼栋管理（含楼栋配置）
 * @author
 */
@RestController
@RequestMapping("/dormitory/building")
@RequiredArgsConstructor
@Tag(name = "楼栋管理")
public class BuildingController {


}
