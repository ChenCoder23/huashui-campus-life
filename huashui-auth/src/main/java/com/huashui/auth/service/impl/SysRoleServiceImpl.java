package com.huashui.auth.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.huashui.auth.domain.pojo.SysRole;
import com.huashui.auth.mapper.SysRoleMapper;
import com.huashui.auth.service.SysRoleService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * 角色Service实现类
 */
@Slf4j
@Service
public class SysRoleServiceImpl
        extends ServiceImpl<SysRoleMapper, SysRole>
        implements SysRoleService {

}