package com.huashui.auth.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.huashui.auth.domain.pojo.SysRole;
import com.huashui.auth.domain.pojo.SysUserRole;
import com.huashui.auth.mapper.SysUserRoleMapper;
import com.huashui.auth.service.SysRoleService;
import com.huashui.auth.service.SysUserRoleService;
import com.huashui.common.enums.RoleCodeType;
import com.huashui.common.exception.BusinessException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


/**
 * 用户角色关联Service实现类
 */
@Slf4j
@Service
public class SysUserRoleServiceImpl extends ServiceImpl<SysUserRoleMapper, SysUserRole> implements SysUserRoleService {

    @Autowired
    private SysRoleService roleService;

    //根据用户的id获取jeus
    @Override
    public String getRoleByuserId(Long userId){
        SysUserRole userRole = lambdaQuery().eq(SysUserRole::getUserId, userId).one();

        if (userRole == null) {
            throw new BusinessException("用户身份错误");
        }

        Long roleId = userRole.getRoleId();

        return roleService.getById(roleId).getRoleCode().getCode();

    }

}