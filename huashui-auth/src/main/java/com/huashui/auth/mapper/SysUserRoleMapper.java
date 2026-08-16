package com.huashui.auth.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import com.huashui.auth.domain.pojo.SysUserRole;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 用户角色关联Mapper
 */
@Mapper
public interface SysUserRoleMapper extends BaseMapper<SysUserRole> {

    /**
     * 根据用户ID查询用户拥有的角色编码
     */
    @Select("""
            SELECT r.role_code
            FROM sys_user_role ur
            INNER JOIN sys_role r ON ur.role_id = r.id
            WHERE ur.user_id = #{userId}
              AND r.status = 1
              AND r.is_deleted = 0
            """)
    List<String> selectRoleCodesByUserId(Long userId);
}