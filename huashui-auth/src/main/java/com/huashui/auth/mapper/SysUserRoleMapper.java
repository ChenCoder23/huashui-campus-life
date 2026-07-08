package com.huashui.auth.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import com.huashui.auth.domain.pojo.SysUserRole;
import org.apache.ibatis.annotations.Mapper;

/**
 * 用户角色关联Mapper
 */
@Mapper
public interface SysUserRoleMapper extends BaseMapper<SysUserRole> {

}