package com.huashui.auth.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;


import com.huashui.api.domain.vo.CleanerSimpleVO;
import com.huashui.auth.domain.pojo.SysUser;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 用户Mapper
 *
 * @author
 */
@Mapper
public interface SysUserMapper extends BaseMapper<SysUser> {

    List<CleanerSimpleVO> selectCleanerByRole(String role);
}