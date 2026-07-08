package com.huashui.auth.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.huashui.auth.domain.pojo.SysUserRole;
import com.huashui.auth.mapper.SysUserRoleMapper;
import com.huashui.auth.service.SysUserRoleService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;


/**
 * 用户角色关联Service实现类
 */
@Slf4j
@Service
public class SysUserRoleServiceImpl
        extends ServiceImpl<SysUserRoleMapper, SysUserRole>
        implements SysUserRoleService {

}