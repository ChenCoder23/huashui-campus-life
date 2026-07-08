package com.huashui.user.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.huashui.common.domain.dto.UserSimpleInfo;
import com.huashui.common.enums.LoginType;
import com.huashui.user.domain.pojo.SysUser;


/**
 * 用户Service
 *
 * @author
 */
public interface SysUserService extends IService<SysUser> {

    UserSimpleInfo getUserInfoByAccount(String account, LoginType type);

    void updateLoginTime(Long userId);
}