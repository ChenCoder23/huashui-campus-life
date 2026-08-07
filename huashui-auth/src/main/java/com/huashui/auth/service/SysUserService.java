package com.huashui.auth.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.huashui.api.domain.vo.CleanerSimpleVO;
import com.huashui.auth.domain.pojo.SysUser;
import com.huashui.common.domain.dto.UserSimpleInfo;
import com.huashui.common.enums.LoginType;


import java.util.List;


/**
 * 用户Service
 *
 * @author
 */
public interface SysUserService extends IService<SysUser> {

    UserSimpleInfo getUserInfoByAccount(String account, LoginType type);

    void updateLoginTime(Long userId);

    List<UserSimpleInfo> getUserInfoList(List<Long> userIds);

    void updateAvatar(Long userId, String avatarUrl);

    void updatePassword(Long id, String password);

    List<CleanerSimpleVO> listByRole(String role);
}