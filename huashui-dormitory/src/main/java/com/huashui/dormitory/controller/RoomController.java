package com.huashui.dormitory.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 房间管理（含床位管理）
 * @author
 */
@RestController
@RequestMapping("/dormitory/room")
@RequiredArgsConstructor
@Tag(name = "房间管理")
public class RoomController {


}
