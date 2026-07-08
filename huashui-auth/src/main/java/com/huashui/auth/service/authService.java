package com.huashui.auth.service;

import com.huashui.auth.domain.dto.LoginDTO;
import com.huashui.auth.domain.vo.CaptchaVO;
import com.huashui.auth.domain.vo.LoginVO;
import com.huashui.common.response.Result;

/**
 * @author
 */
public interface authService {

    //生成图片验证码
    Result<CaptchaVO> getCaptcha();

    //用户账密登录
    Result<LoginVO> userLogin(LoginDTO dto);

    void logout();
}
