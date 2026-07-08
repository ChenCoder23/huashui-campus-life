package com.huashui.auth.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.captcha.CaptchaUtil;
import cn.hutool.captcha.LineCaptcha;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.digest.BCrypt;
import com.huashui.api.client.user.UserClient;
import com.huashui.auth.domain.dto.LoginDTO;
import com.huashui.auth.domain.dto.PasswordDTO;
import com.huashui.auth.domain.vo.CaptchaVO;
import com.huashui.auth.domain.vo.LoginVO;
import com.huashui.common.domain.dto.UserSimpleInfo;
import com.huashui.common.enums.LoginType;
import com.huashui.auth.service.authService;
import com.huashui.common.constants.RedisConstants;
import com.huashui.common.enums.Status;
import com.huashui.common.enums.error.ErrorType;
import com.huashui.common.exception.BusinessException;
import com.huashui.common.response.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.concurrent.TimeUnit;

/**
 * @author
 */

@Slf4j
@Service
public class authServiceImpl implements authService {


    @Autowired
    private  StringRedisTemplate redisTemplate;

    @Autowired
    private UserClient userClient;





    //生成图片验证码
    @Override
    public Result<CaptchaVO> getCaptcha() {

        // todo IP 请求验证码防刷

        //生成4位数字的验证码
        LineCaptcha captcha = CaptchaUtil.createLineCaptcha(120 , 40 , 4 , 5);
        String code = captcha.getCode();
        log.info("{},获取验证码:{}", LocalDateTime.now(),code);
        String key = IdUtil.fastSimpleUUID();

        //把key和code放了redis便于比较
        // 存 Redis，5 分钟过期
        redisTemplate.opsForValue().set(
                RedisConstants.CAPTCHA_PREFIX + key,
                code,
                RedisConstants.CAPTCHA_EXPIRE,  //设置过期时间了  10 分钟
                TimeUnit.MINUTES
        );

         // base64 图片（Hutool getImageBase64Data 已含 data:image/png;base64, 前缀）
         String imageBase64 = captcha.getImageBase64Data();

        return Result.ok(new CaptchaVO(key,imageBase64));

    }



    //用户的账密登录
    @Override
    public Result<LoginVO> userLogin(LoginDTO dto) {
        if (StrUtil.isBlank(dto.getCaptchaCode())){
            throw new BusinessException("验证码不能为空");
        }
        // 1. 验证码校验
        if (StrUtil.isNotBlank(dto.getCaptchaKey())) {
            //获取缓存里的验证码
            String cachedCode = redisTemplate.opsForValue()
                    .get(RedisConstants.CAPTCHA_PREFIX + dto.getCaptchaKey());
            if (StrUtil.isBlank(cachedCode)) {
                throw new BusinessException(ErrorType.CODE_NOT_EXISTS);
            }
            if (!cachedCode.equalsIgnoreCase(dto.getCaptchaCode())) {
                throw new BusinessException(ErrorType.CODE_ERROR);
            }
            //获取后删除验证码
            redisTemplate.delete(RedisConstants.CAPTCHA_PREFIX + dto.getCaptchaKey());
        }

        String account = dto.getAccount();
        LoginType loginType = dto.getLoginType();
        //账号不能为null,密码判空
        if (StrUtil.isBlank(account) || StrUtil.isBlank(dto.getPassword())) {
            throw new BusinessException("账号密码不能为空");
        }

        UserSimpleInfo userInfo = userClient.getUserInfo(account, loginType);

        if (userInfo == null) {
            throw new BusinessException(ErrorType.USER_NOT_FOUND);
        }

        if (Status.DISABLED == userInfo.getStatus() ) {
            throw new BusinessException("账号已被冻结，请联系管理员");
        }

        // 3. 验证密码 BCrypt加密存储
        //todo 密码错误建议增加失败次数限制 登录成功后清除失败次数
        if (!BCrypt.checkpw(dto.getPassword(), userInfo.getPassword())) {
            throw new BusinessException("用户名或密码错误");
        }
        // 4. Sa-Token 登录
        log.info("准备执行SaToken登录");

        StpUtil.login(userInfo.getId());

        log.info("SaToken登录完成");
        // 5. 更新最后登录时间
        userClient.updateLoginTime(userInfo.getId());

        // 6. 返回

        LoginVO loginVO = LoginVO.builder()
                .token(StpUtil.getTokenValue())
                .userId(userInfo.getId())
                .username(userInfo.getUsername())
                .realName(userInfo.getRealName())
                // todo 添加角色信息
                .avatar(userInfo.getAvatar())
                .build();
        return Result.ok(loginVO);


    }

    //退出登录
    @Override
    public void logout() {
        StpUtil.logout();
    }


    // 修改密码
    @Override
    public void updatePassword(PasswordDTO dto) {
        //判断当前用户身份
    }


}
