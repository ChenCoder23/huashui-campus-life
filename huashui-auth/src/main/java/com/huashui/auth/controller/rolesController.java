package com.huashui.auth.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author
 */
@Tag(name = "认证中心", description = "登录、注册、注销、验证码")
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class rolesController {

    // todo /auth/roles	GET/POST	角色列表 / 新增角色

    // todo /auth/roles/{id}	PUT/DELETE	编辑 / 删除角色


}
