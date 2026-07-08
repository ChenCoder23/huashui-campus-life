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
public class menuController {

    // todo /auth/menus	GET	全量菜单树（供超管配置用）

    // todo /auth/menus	POST	新增菜单

    // todo /auth/menus/{id}	PUT/DELETE	编辑 / 删除菜单

    // todo /auth/roles/{id}/menus	GET/PUT	查询 / 设置角色拥有的菜单权限

    // todo /auth/users/{id}/roles	GET/PUT	查询 / 设置用户拥有的角色
}
