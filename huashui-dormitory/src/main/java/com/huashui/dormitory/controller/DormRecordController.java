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


}
