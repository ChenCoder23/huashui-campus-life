package com.huashui.auth.service;

import com.huashui.auth.domain.dto.LoginDTO;
import com.huashui.auth.domain.dto.PasswordDTO;
import com.huashui.auth.domain.vo.CaptchaVO;
import com.huashui.auth.domain.vo.LoginVO;
import com.huashui.common.response.Result;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

/**
 * @author
 */
public interface authService {

    //生成图片验证码
    Result<CaptchaVO> getCaptcha(String remoteAddr);

    //用户账密登录
    Result<LoginVO> userLogin(LoginDTO dto, String remoteAddr);

    void logout();

    void updatePassword(PasswordDTO dto);

    void updateAvatar(String avatarUrl);



    void setEmailCode(String email);

    boolean verifyCode(String email, String inputCode);

    String EmailLogin(@Email String email, @NotBlank String code);
}
