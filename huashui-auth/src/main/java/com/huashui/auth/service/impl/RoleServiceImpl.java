package com.huashui.auth.service.impl;


import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.huashui.auth.domain.dto.RoleUpdateDTO;

import com.huashui.auth.domain.pojo.SysRole;
import com.huashui.auth.domain.pojo.SysUserRole;
import com.huashui.auth.domain.vo.RoleVO;
import com.huashui.auth.mapper.SysRoleMapper;
import com.huashui.auth.mapper.SysUserRoleMapper;
import com.huashui.auth.service.SysRoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.List;


@Service
@RequiredArgsConstructor
public class RoleServiceImpl extends ServiceImpl<SysRoleMapper , SysRole> implements SysRoleService{

    private final SysRoleMapper roleMapper;

    private final SysUserRoleMapper userRoleMapper;



    /**
     * 查询用户角色
     */
    @Override
    public RoleVO getUserRole(Long userId) {
        return roleMapper.selectUserRole(userId);
    }



    /**
     * 设置用户角色
     *
     * 一个用户只允许一个角色
     */
    @Override
    @Transactional
    public void setUserRole(Long userId, Long roleId) {


        // 删除用户原来的角色
        userRoleMapper.delete(Wrappers.<SysUserRole>lambdaQuery().eq(SysUserRole::getUserId,userId));

        // 添加新的角色关系
        SysUserRole userRole = new SysUserRole();

        userRole.setUserId(userId);
        userRole.setRoleId(roleId);
        //插入
        userRoleMapper.insert(userRole);

    }



    /**
     * 查询角色列表
     */
    @Override
    public List<RoleVO> listRoles() {
        return roleMapper.selectRoleList();
    }


    /**
     * 修改角色
     */
    @Override
    public void updateRole(Long id, RoleUpdateDTO dto) {

        LambdaUpdateWrapper<SysRole> wrapper = Wrappers.lambdaUpdate();
        wrapper.eq(SysRole::getId,id)
                .set(SysRole::getRoleName,dto.getRoleName())
                .set(SysRole::getDescription,dto.getDescription())
                .set(SysRole::getSortOrder,dto.getSortOrder())
                .set(SysRole::getStatus,dto.getStatus());
        roleMapper.update(null,wrapper);

    }

}