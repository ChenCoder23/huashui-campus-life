package com.huashui.auth.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.huashui.auth.domain.dto.RoleUpdateDTO;
import com.huashui.auth.domain.pojo.SysRole;
import com.huashui.auth.domain.vo.RoleVO;

import java.util.List;

/**
 * 角色Service
 */
public interface SysRoleService extends IService<SysRole> {

    RoleVO getUserRole(Long id);

    void setUserRole(Long id, Long roleId);

    List<RoleVO> listRoles();

    void updateRole(Long id, RoleUpdateDTO dto);
}