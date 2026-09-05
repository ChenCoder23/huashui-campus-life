package com.huashui.auth.controller;

import cn.hutool.crypto.digest.BCrypt;
import com.huashui.auth.domain.dto.CreateStaffDTO;
import com.huashui.auth.domain.dto.AdminUserPageDTO;
import com.huashui.auth.domain.vo.AdminUserVO;
import com.huashui.auth.domain.pojo.SysRole;
import com.huashui.auth.domain.pojo.SysUser;
import com.huashui.auth.domain.pojo.SysUserRole;
import com.huashui.auth.mapper.SysRoleMapper;
import com.huashui.auth.mapper.SysUserMapper;
import com.huashui.auth.mapper.SysUserRoleMapper;
import com.huashui.auth.service.AdminUserService;
import com.huashui.common.enums.RoleCodeType;
import com.huashui.common.enums.Status;
import com.huashui.common.exception.BusinessException;
import com.huashui.common.response.PageResult;
import com.huashui.common.response.Result;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/auth/admin/users")
@RequiredArgsConstructor
public class AdminUserController {

    private final SysUserMapper userMapper;
    private final SysRoleMapper roleMapper;
    private final SysUserRoleMapper userRoleMapper;
    private final AdminUserService adminUserService;

    @GetMapping("/page")
    public Result<PageResult<AdminUserVO>> page(@Valid AdminUserPageDTO dto) {
        return Result.ok(adminUserService.page(dto));
    }

    @PostMapping
    public Result<Void> create(@Valid @RequestBody CreateStaffDTO dto) {
        Long exists = userMapper.selectCount(
                new com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper<SysUser>()
                        .eq(SysUser::getUsername, dto.getUsername()));
        if (exists != null && exists > 0) {
            throw new BusinessException("该账号已存在");
        }

        SysUser user = new SysUser();
        user.setUsername(dto.getUsername());
        user.setPassword(BCrypt.hashpw(dto.getPassword(), BCrypt.gensalt()));
        user.setRealName(dto.getRealName());
        user.setPhone(dto.getPhone());
        user.setEmail(dto.getEmail());
        user.setCampusId(dto.getCampusId());
        user.setMajor(dto.getMajor());
        user.setGrade(dto.getGrade());
        user.setStatus(Status.ENABLED);
        user.setIsDeleted(0);
        userMapper.insert(user);

        SysRole role = roleMapper.selectOne(
                new com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper<SysRole>()
                        .eq(SysRole::getRoleCode, RoleCodeType.valueOf(dto.getRoleCode())));
        if (role == null) {
            throw new BusinessException("角色不存在");
        }

        SysUserRole userRole = new SysUserRole();
        userRole.setUserId(user.getId());
        userRole.setRoleId(role.getId());
        userRoleMapper.insert(userRole);
        return Result.ok();
    }

    @PutMapping("/{id}/status")
    public Result<Void> updateStatus(@PathVariable Long id, @RequestParam Integer status) {
        SysUser user = userMapper.selectById(id);
        if (user == null) {
            throw new BusinessException("用户不存在");
        }
        user.setStatus(status == 1 ? Status.ENABLED : Status.DISABLED);
        userMapper.updateById(user);
        return Result.ok();
    }

    @PutMapping("/{id}/password")
    public Result<Void> resetPassword(@PathVariable Long id, @RequestBody Map<String, String> body) {
        String password = body.get("password");
        if (password == null || password.isBlank()) {
            throw new BusinessException("密码不能为空");
        }
        SysUser user = userMapper.selectById(id);
        if (user == null) {
            throw new BusinessException("用户不存在");
        }
        user.setPassword(BCrypt.hashpw(password, BCrypt.gensalt()));
        userMapper.updateById(user);
        return Result.ok();
    }
}
