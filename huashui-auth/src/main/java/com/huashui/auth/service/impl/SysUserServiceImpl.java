package com.huashui.auth.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;


import com.huashui.api.domain.vo.task.CleanerSimpleVO;
import com.huashui.auth.domain.pojo.SysUser;
import com.huashui.auth.mapper.SysUserMapper;
import com.huashui.auth.service.SysUserService;
import com.huashui.common.domain.dto.UserSimpleInfo;
import com.huashui.common.enums.Status;
import com.huashui.common.enums.error.ErrorType;
import com.huashui.common.enums.LoginType;
import com.huashui.common.exception.BusinessException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * 用户Service实现类
 *
 * @author
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class SysUserServiceImpl extends ServiceImpl<SysUserMapper, SysUser> implements SysUserService {

    private final SysUserMapper userMapper;


    @Override
    public UserSimpleInfo getUserInfoByAccount(String account, LoginType type) {

        log.info("本次登录账号:{} ,本次登录方式:{}",account,type.getDesc());
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
                log.info("user =  {}",user);
                break;
        }
        if(user == null){
            throw new BusinessException(ErrorType.USER_NOT_FOUND);
        }
        UserSimpleInfo userSimpleInfo = BeanUtil.copyProperties(user, UserSimpleInfo.class);


        return userSimpleInfo;
    }

    //更新用户的最后登录时间
    @Override
    public void updateLoginTime(Long userId) {
        SysUser user = new SysUser();
        user.setId(userId);
        user.setLastLoginTime(LocalDateTime.now());
    }

    @Override
    public List<UserSimpleInfo> getUserInfoList(List<Long> userIds) {

        List<SysUser> sysUserList =new ArrayList<>();

        for (Long userId : userIds) {
            SysUser user = lambdaQuery().eq(SysUser::getId, userId)
                    .eq(SysUser::getStatus, Status.ENABLED).one();
            if (user != null) {
                sysUserList.add(user);
            }
        }
        return BeanUtil.copyToList(sysUserList, UserSimpleInfo.class);
    }

    @Override
    public void updateAvatar(Long userId, String avatarUrl) {
        lambdaUpdate()
                .eq(SysUser::getId, userId)
                .set(SysUser::getAvatar, avatarUrl)
                .update();
    }


    //更新用户密码
    @Override
    public void updatePassword(Long id, String password) {
        lambdaUpdate().eq(SysUser::getId,id)
                .set(SysUser::getPassword,password)
                .update();
    }

    @Override
    public List<CleanerSimpleVO> listByRole(String role) {
        return userMapper.selectCleanerByRole(role);
    }

    @Override
    public UserSimpleInfo getUserInfoById(Long id) {
        SysUser user = getById(id);
        UserSimpleInfo vo = new UserSimpleInfo();

        BeanUtils.copyProperties(user,vo);;

        return vo;
    }


}