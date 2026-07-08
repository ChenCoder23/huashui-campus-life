package com.huashui.api.fallback;

import com.huashui.api.client.user.UserClient;
import com.huashui.common.domain.dto.UserSimpleInfo;
import com.huashui.common.enums.LoginType;
import com.huashui.common.response.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class UserClientFallbackFactory
        implements FallbackFactory<UserClient> {


    @Override
    public UserClient create(Throwable throwable) {


        log.error("查询用户信息服务异常:",throwable);
        return new UserClient() {
            @Override
            public UserSimpleInfo getUserInfo(String account, LoginType type) {
                return null;
            }
        };

    }
}