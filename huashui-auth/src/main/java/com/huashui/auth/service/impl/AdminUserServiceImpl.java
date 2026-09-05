package com.huashui.auth.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.huashui.auth.domain.dto.AdminUserPageDTO;
import com.huashui.auth.domain.pojo.SysRole;
import com.huashui.auth.domain.pojo.SysUser;
import com.huashui.auth.domain.pojo.SysUserRole;
import com.huashui.auth.domain.vo.AdminUserVO;
import com.huashui.auth.mapper.SysRoleMapper;
import com.huashui.auth.mapper.SysUserMapper;
import com.huashui.auth.mapper.SysUserRoleMapper;
import com.huashui.auth.service.AdminUserService;
import com.huashui.common.exception.BusinessException;
import com.huashui.common.response.PageResult;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminUserServiceImpl implements AdminUserService {

    private final SysRoleMapper roleMapper;
    private final SysUserRoleMapper userRoleMapper;
    private final SysUserMapper userMapper;

    @Override
    public PageResult<AdminUserVO> page(AdminUserPageDTO dto) {
        SysRole role = roleMapper.selectOne(new LambdaQueryWrapper<SysRole>()
                .eq(SysRole::getRoleName, dto.getRoleName()));
        if (role == null) {
            throw new BusinessException("角色不存在");
        }

        List<Long> userIds = userRoleMapper.selectList(new LambdaQueryWrapper<SysUserRole>()
                        .select(SysUserRole::getUserId)
                        .eq(SysUserRole::getRoleId, role.getId()))
                .stream()
                .map(SysUserRole::getUserId)
                .distinct()
                .toList();
        if (userIds.isEmpty()) {
            return PageResult.of(0, dto.getPageNum(), dto.getPageSize(), List.of());
        }

        LambdaQueryWrapper<SysUser> queryWrapper = new LambdaQueryWrapper<SysUser>()
                .in(SysUser::getId, userIds);
        if (StringUtils.hasText(dto.getKeyword())) {
            queryWrapper.and(wrapper -> wrapper
                    .like(SysUser::getUsername, dto.getKeyword())
                    .or()
                    .like(SysUser::getRealName, dto.getKeyword())
                    .or()
                    .like(SysUser::getPhone, dto.getKeyword()));
        }
        queryWrapper.orderByDesc(SysUser::getId);

        Page<SysUser> page = userMapper.selectPage(
                new Page<>(dto.getPageNum(), dto.getPageSize()), queryWrapper);
        List<AdminUserVO> records = page.getRecords().stream()
                .map(this::toVO)
                .toList();
        return PageResult.of(page.getTotal(), page.getCurrent(), page.getSize(), records);
    }

    private AdminUserVO toVO(SysUser user) {
        AdminUserVO vo = new AdminUserVO();
        vo.setId(user.getId());
        vo.setUsername(user.getUsername());
        vo.setRealName(user.getRealName());
        vo.setPhone(user.getPhone());
        vo.setEmail(user.getEmail());
        vo.setAvatar(user.getAvatar());
        vo.setStatus(user.getStatus() == null ? null : user.getStatus().getValue());
        vo.setCreateTime(user.getCreateTime());
        return vo;
    }
}
