package com.huashui.auth.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.captcha.CaptchaUtil;
import cn.hutool.captcha.LineCaptcha;
import cn.hutool.core.lang.UUID;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.digest.BCrypt;
import com.huashui.api.client.user.UserClient;
import com.huashui.auth.domain.dto.BindEmailDTO;
import com.huashui.auth.domain.dto.LoginDTO;
import com.huashui.auth.domain.dto.PasswordDTO;
import com.huashui.auth.domain.vo.CaptchaVO;
import com.huashui.auth.domain.vo.LoginVO;
import com.huashui.auth.service.SysUserRoleService;
import com.huashui.auth.util.email.EmailUtil;
import com.huashui.auth.util.email.VerifyCodeUtil;
import com.huashui.common.constants.MQConstants;
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
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
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

    @Autowired
    private  Executor taskExecutor;

    @Autowired
    private RabbitTemplate mqTemplate;




    //生成图片验证码
    @Override
    public Result<CaptchaVO> getCaptcha(String ip) {
        //校验生成次数
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
        checkCaptcha(dto.getCaptchaCode(), RedisConstants.CAPTCHA_PREFIX+dto.getCaptchaKey());

        String account = dto.getAccount();
        LoginType loginType = dto.getLoginType();
        //账号不能为null,密码判空
        if (StrUtil.isBlank(account) || StrUtil.isBlank(dto.getPassword())) {
            throw new BusinessException("账号密码不能为空");
        }

        // todo 首先查redis 查不到再查数据库
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
        // 获取用户角色编码，存入 Session 供 Gateway 透传
        String roleCode = userRoleService.getRoleByuserId(userInfo.getId());
        StpUtil.getSession().set("role", roleCode);

        //  消息队列 更新登录时间
        mqTemplate.convertAndSend(
                MQConstants.TOPIC_EXCHANGE,
                MQConstants.UPDATE_LOGIN_KEY,
                userInfo.getId()
        );

        CompletableFuture.runAsync(()->{
            //清除图像验证码请求次数
            cleanLoginCaptchaNum(ip);
            //清除账号密码不匹配的次数
            cleanAccountLoginLimit(account);
        },taskExecutor);

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
                    .get(key);
            if (StrUtil.isBlank(cachedCode)) {
                throw new BusinessException(ErrorType.CODE_NOT_EXISTS);
            }
            if (!cachedCode.equalsIgnoreCase(code)) {
                throw new BusinessException(ErrorType.CODE_ERROR);
            }
            //获取后删除验证码
            redisTemplate.delete(key);
        }
    }


    // 修改头像
    @Override
    public void updateAvatar(String avatarUrl) {
        Long userId = StpUtil.getLoginIdAsLong();
        userClient.updateAvatar(userId, avatarUrl);
        // todo 发送MQ异步修改  回填业务id

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
        Boolean set = redisTemplate.opsForValue().setIfAbsent(limitKey, "1L", 60, TimeUnit.SECONDS);
        if (Boolean.FALSE.equals(set)) {
            throw new BusinessException("发送太频繁，请稍后再试");
        }
        // 2. 生成验证码
        String code = VerifyCodeUtil.generateCode();
        // 3. 存入Redis，5分钟过期
        String codeKey = "email:code:" + email;
        redisTemplate.opsForValue().set(codeKey, code, 5*60, TimeUnit.SECONDS);
        //  使用线程池异步发送 邮件
            CompletableFuture.runAsync(() -> {
                try {
                    emailService.sendVerifyCode(email, code);
                } catch (Exception e) {
                    log.error("邮件发送失败,email={}", email, e);
                    redisTemplate.delete(limitKey);
                    redisTemplate.delete(codeKey);
                }
            }, taskExecutor);

    }


    @Override
    public String EmailLogin(String email, String code) {
        // 1. 校验验证码
        checkCaptcha(code ,RedisConstants.EMAIL_PREFIX+email);

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


    // 绑定邮箱
    @Override
    public void bindEmail(BindEmailDTO dto) {
        // 1. 校验邮箱格式
        boolean qqEmail = EmailUtil.isQQEmail(dto.getEmail());
        if (!qqEmail){
            throw new BusinessException("邮箱格式不正确");
        }
        // 2. 获取当前用户id
        Long userId = StpUtil.getLoginIdAsLong();

        // 3. 查询用户
        List<UserSimpleInfo> userInfoList = userClient.getUserInfoList(Collections.singletonList(userId));
        UserSimpleInfo user = userInfoList.get(0);
        if(user == null){
            throw new BusinessException("用户不存在");
        }
        // 4. 判断当前用户是否已经绑定邮箱
        if(StrUtil.isNotBlank(user.getEmail())){
            throw new BusinessException("当前账号已经绑定邮箱");
        }

        // 5. 判断邮箱是否已经被其他用户绑定

        // 7. 校验验证码
        checkCaptcha(dto.getCode(),RedisConstants.EMAIL_PREFIX+dto.getEmail());

        // 8. 保存邮箱


    }


    //todo
    @Override
    public void updateEmail(BindEmailDTO dto) {
        //校验邮箱格式

        //获取当前用户的id 用户是否存在

        //判断当前用户的是否已经绑定

        //判断当前邮箱是否已经被绑定

        //从redis里获取验证码

        // 校验验证码

        //更新
    }



   //定义脚本常量
    private  static final DefaultRedisScript<Long> CHECK_LIMIT_SCRIPT;

    static {
        CHECK_LIMIT_SCRIPT = new DefaultRedisScript<>(
                """
                        local count = redis.call('incr', KEYS[1])
                        
                        if count == 1 then
                            redis.call('expire', KEYS[1], ARGV[1])
                        end
                        
                        return count
                        """,
                Long.class
        );
    }

    //判断IP地址是否达到图形化验证码的请求次数上限
    //一分钟最多请求5次图像验证码
    public boolean checkIpLimit(String ip) {
        //设置IP的key
       String key = RedisConstants.CAPTCHA_PREFIX_NUM + ip;

        Long count = redisTemplate.execute(
                CHECK_LIMIT_SCRIPT,
                Collections.singletonList(key),
                "60"
        );
        return count <= 5;

    }


    // 清除指定IP地址的图像验证码的请求次数
    public void cleanLoginCaptchaNum(String ip){

        //设置IP的key
        String key = RedisConstants.CAPTCHA_PREFIX_NUM + ip;


        redisTemplate.delete(key);
    }

    //判断账号和密码是否达到错误次数上限
    //每分钟可尝试五次
    public boolean checkLoginNumLimit(String account) {

        //设置key
        String key =RedisConstants.ACCOUNT_LOGIN_NUM + account;
        Long count = redisTemplate.execute(
                CHECK_LIMIT_SCRIPT,
                Collections.singletonList(key),
                "60"
        );
        return count <= 5;
    }

    //清除account和密码不匹配的次数
    public void  cleanAccountLoginLimit(String account) {
        //设置key
        String key =RedisConstants.ACCOUNT_LOGIN_NUM + account;
        redisTemplate.delete(key);

    }


}
