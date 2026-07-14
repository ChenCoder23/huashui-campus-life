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
import com.huashui.auth.service.SysUserRoleService;
import com.huashui.auth.util.email.EmailUtil;
import com.huashui.auth.util.email.VerifyCodeUtil;
import com.huashui.common.domain.dto.UserSimpleInfo;
import com.huashui.common.enums.LoginType;
import com.huashui.auth.service.authService;
import com.huashui.common.constants.RedisConstants;
import com.huashui.common.enums.RoleCodeType;
import com.huashui.common.enums.Status;
import com.huashui.common.enums.error.ErrorType;
import com.huashui.common.exception.BusinessException;
import com.huashui.common.response.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * @author
 */

@Slf4j
@Service
public class authServiceImpl implements authService {




    @Autowired
    private EmailService emailService;

    @Autowired
    private  StringRedisTemplate redisTemplate;

    @Autowired
    private UserClient userClient;

    @Autowired
    private SysUserRoleService userRoleService;


    //生成图片验证码
    @Override
    public Result<CaptchaVO> getCaptcha(String ip) {


        if (! checkIpLimit(ip)) {
            throw new BusinessException("获取验证码次数上限,请稍后再试");
        }


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
    public Result<LoginVO> userLogin(LoginDTO dto, String ip) {

        // 校验验证码
        checkCaptcha(dto.getCaptchaCode(), dto.getCaptchaKey());

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
        if (!checkLoginNumLimit(account)) {
            throw new BusinessException("该账号被锁定稍后再试");
        }
        if (!BCrypt.checkpw(dto.getPassword(), userInfo.getPassword())) {
            throw new BusinessException("用户名或密码错误");
        }
        // 4. Sa-Token 登录

        StpUtil.login(userInfo.getId());


        // todo 消息队列 跟新登录时间
        userClient.updateLoginTime(userInfo.getId());

        //清除图像验证码请求次数
        cleanLoginCaptchaNum(ip);
        //清除账号密码不匹配的次数
        cleanAccountLoginLimit(account);

        //获取用户角色编码
        String roleCode = userRoleService.getRoleByuserId(userInfo.getId());

        // 6. 返回
        LoginVO loginVO = LoginVO.builder()
                .token(StpUtil.getTokenValue())
                .userId(userInfo.getId())
                .username(userInfo.getUsername())
                .realName(userInfo.getRealName())
                .userType(roleCode)
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
        //获取用户id
        Long userId = StpUtil.getLoginIdAsLong();
        //判断当前用户身份
        String roleCode = userRoleService.getRoleByuserId(userId);
        //管理员修改密码
        if (Objects.equals(roleCode, RoleCodeType.SUPER_ADMIN.getCode())
                || Objects.equals(roleCode, RoleCodeType.DORM_MANAGER.getCode())){
            updateUserPassword(userId,dto.getNewPassword());
        }else {
            // 非管理员修改密码
            checkCaptcha(dto.getCaptchaCode(), dto.getCaptchaKey());
            UserSimpleInfo userInfo = userClient.getUserInfo(dto.getAccount(), LoginType.ACCOUNT);
            if (!Objects.equals(userInfo.getId(), userId)){
                throw new BusinessException("你无法修改他人的密码");
            }
            // todo 校验旧密码
            updateUserPassword(userId,dto.getNewPassword());

        }

    }

    //更新用户密码
    private void updateUserPassword(Long id,String password){
        password = BCrypt.hashpw(password);
        userClient.updatePassword(id,password);
    }


    //校验验证码
    private void checkCaptcha(String code, String key) {
        if (StrUtil.isBlank(code)) {
            throw new BusinessException("验证码不能为空");
        }
        // 1. 验证码校验
        if (StrUtil.isNotBlank(key)) {
            //获取缓存里的验证码
            String cachedCode = redisTemplate.opsForValue()
                    .get(RedisConstants.CAPTCHA_PREFIX + key);
            if (StrUtil.isBlank(cachedCode)) {
                throw new BusinessException(ErrorType.CODE_NOT_EXISTS);
            }
            if (!cachedCode.equalsIgnoreCase(code)) {
                throw new BusinessException(ErrorType.CODE_ERROR);
            }
            //获取后删除验证码
            redisTemplate.delete(RedisConstants.CAPTCHA_PREFIX + key);
        }
    }


    // 修改头像
    @Override
    public void updateAvatar(String avatarUrl) {
        Long userId = StpUtil.getLoginIdAsLong();
        userClient.updateAvatar(userId, avatarUrl);
    }

    //发送邮箱验证码
    @Override
    public void setEmailCode(String email) {
        //校验邮箱格式
        if (! EmailUtil.isQQEmail(email)) {
            throw new BusinessException("邮箱格式不正确,请输入正确的QQ邮箱");
        }

        // 1. 防刷：60秒内不能重复发送
        String limitKey = "email:limit" + email;
        Boolean hasLimit = redisTemplate.hasKey(limitKey);
        if (Boolean.TRUE.equals(hasLimit)) {
            throw new BusinessException("发送太频繁，请稍后再试");
        }
        // 2. 生成验证码
        String code = VerifyCodeUtil.generateCode();

        // 3. 存入Redis，5分钟过期
        String codeKey = "email:code:" + email;
        redisTemplate.opsForValue().set(codeKey, code, 5*60, TimeUnit.SECONDS);
        // 4. 设置发送频率限制标记
        redisTemplate.opsForValue().set(limitKey, "1", 60, TimeUnit.SECONDS);

        // 5. 发送邮件
        emailService.sendVerifyCode(email, code);
    }


    /**
     * 校验验证码（登录时调用）
     * 校验成功后立即删除，防止重复使用
     */
    @Override
    public boolean verifyCode(String email, String inputCode) {
        String codeKey = "email:code:" + email;
        String cachedCode = redisTemplate.opsForValue().get(codeKey);

        if (cachedCode == null) {
            throw new BusinessException("验证码已过期，请重新获取");
        }

        boolean match = cachedCode.equals(inputCode);
        if (match) {
            redisTemplate.delete(codeKey); // 一次性使用，验证成功立即删除
        }
        return match;
    }

    @Override
    public String EmailLogin(String email, String code) {
        // 1. 校验验证码
        boolean valid = verifyCode(email, code);
        if (!valid) {
            throw new BusinessException("验证码错误");
        }

        // 2. 查询用户是否存在
        UserSimpleInfo userInfo = userClient.getUserInfo(email, LoginType.EMAIL);

        if (userInfo == null){
            throw  new BusinessException("该用户不存在");
        }

        Long userId = userInfo.getId();
        // 3. Sa-Token 登录签发token
        StpUtil.login(userId);

        // todo 消息队列 跟新登录时间
        userClient.updateLoginTime(userId);
        return StpUtil.getTokenValue();
    }


    //判断IP地址是否达到图形化验证码的请求次数上限
    public boolean checkIpLimit(String ip) {

        //如果无法获取Ip
        if (StrUtil.isBlank(ip)) {
            log.warn("获取客户端IP失败");
            return true;
        }


        //设置IP的key
       String key = RedisConstants.CAPTCHA_PREFIX_NUM + ip;

        //todo 使用lua保证原子性
        Long count =
                redisTemplate.opsForValue()
                        .increment(key);

        //第一次访问设置过期时间
        if (count == 1) {
            redisTemplate.expire(
                    key,
                    60,
                    TimeUnit.SECONDS
            );
        }


        return count <= 5;
    }


    // 清除指定IP地址的图像验证码的请求次数
    public void cleanLoginCaptchaNum(String ip){

        //设置IP的key
        String key = RedisConstants.CAPTCHA_PREFIX_NUM + ip;


        redisTemplate.delete(key);
    }

    //判断账号和密码是否达到错误次数上限
    public boolean checkLoginNumLimit(String account) {

        //设置key
        String key =RedisConstants.ACCOUNT_LOGIN_NUM + account;

        //todo 使用lua保证原子性
        Long count =
                redisTemplate.opsForValue()
                        .increment(key);

        //第一次访问设置过期时间
        if (count == 1) {
            redisTemplate.expire(
                    key,
                    60,
                    TimeUnit.SECONDS
            );
        }


        return count <= 5;
    }

    //清除account和密码不匹配的次数
    public void  cleanAccountLoginLimit(String account) {
        //设置key
        String key =RedisConstants.ACCOUNT_LOGIN_NUM + account;
        redisTemplate.delete(key);

    }


}
