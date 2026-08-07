package com.huashui.auth.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import com.huashui.auth.domain.pojo.SysRole;
import com.huashui.auth.domain.vo.RoleVO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 角色Mapper
 */
@Mapper
public interface SysRoleMapper extends BaseMapper<SysRole> {

    RoleVO selectUserRole(Long userId);

    List<RoleVO> selectRoleList();
}