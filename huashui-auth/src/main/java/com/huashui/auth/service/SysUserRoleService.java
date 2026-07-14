package com.huashui.auth.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.huashui.auth.domain.pojo.SysUserRole;

/**
 * 用户角色关联Service
 */
public interface SysUserRoleService extends IService<SysUserRole> {

    //根据用户的id获取jeus
    String getRoleByuserId(Long userId);
}