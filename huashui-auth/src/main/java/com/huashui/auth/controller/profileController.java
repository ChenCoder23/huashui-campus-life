package com.huashui.auth.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author
 */

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class profileController {

    // todo /auth/profile	GET	登录后一次性返回：用户信息 + 角色列表 + 权限码扁平列表 + 菜单树

    // todo /auth/password	PUT	修改密码（原密码 → 新密码）
}
