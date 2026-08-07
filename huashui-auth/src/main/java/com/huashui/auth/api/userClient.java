package com.huashui.auth.api;

import com.huashui.api.domain.vo.CleanerSimpleVO;
import com.huashui.auth.service.SysUserService;
import com.huashui.common.domain.dto.UserSimpleInfo;
import com.huashui.common.enums.LoginType;

import com.huashui.common.response.Result;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/user/inner")
public class userClient {


    @Resource
    private SysUserService userService;


    // 根据手机号/邮箱/账号(学号/工号)查询用户信息
    @GetMapping("/userInfo")
    public UserSimpleInfo getUserInfo(
            @RequestParam("account") String account ,
            @RequestParam("type") LoginType type){
        return userService.getUserInfoByAccount(account, type);
    }



    // 根据Id列表查询用户信息
    @GetMapping("/list")
    public List<UserSimpleInfo> getUserInfoList(@RequestParam List<Long> userIds) {
        return userService.getUserInfoList(userIds);
    }

    /**
     * 根据角色查询用户
     *
     * @param role 角色编码
     */
    @GetMapping("/user/listByRole")
    Result<List<CleanerSimpleVO>> listByRole(@RequestParam("role") String role){
        return Result.ok(userService.listByRole(role));
    }


}