package com.huashui.user.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;


import com.huashui.common.domain.dto.UserSimpleInfo;
import com.huashui.common.enums.error.ErrorType;
import com.huashui.common.enums.LoginType;
import com.huashui.common.exception.BusinessException;
import com.huashui.user.domain.pojo.SysUser;
import com.huashui.user.mapper.SysUserMapper;
import com.huashui.user.service.SysUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * 用户Service实现类
 *
 * @author
 */
@Slf4j
@Service
public class SysUserServiceImpl extends ServiceImpl<SysUserMapper, SysUser> implements SysUserService {


    @Override
    public UserSimpleInfo getUserInfoByAccount(String account, LoginType type) {
        SysUser user = null;
        switch(type){
            case PHONE:
                //手机号查询用户;
                 user = lambdaQuery().eq(SysUser::getPhone, account).one();
                break;

            case ACCOUNT:
                //账号(学号/工号)查询用户;
                user = lambdaQuery().eq(SysUser::getUsername, account).one();
                break;

            case EMAIL:
                //邮箱查询用户;
                user = lambdaQuery().eq(SysUser::getEmail, account).one();
                break;
        }
        if(user == null){
            throw new BusinessException(ErrorType.USER_NOT_FOUND);
        }
        UserSimpleInfo userSimpleInfo = BeanUtil.copyProperties(user, UserSimpleInfo.class);
        return userSimpleInfo;
    }



}