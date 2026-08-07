package com.huashui.api.fallback;

import com.huashui.api.client.user.UserClient;
import com.huashui.api.domain.vo.CleanerSimpleVO;
import com.huashui.common.domain.dto.UserSimpleInfo;
import com.huashui.common.enums.LoginType;
import com.huashui.common.response.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
public class UserClientFallbackFactory
        implements FallbackFactory<UserClient> {


    @Override
    public UserClient create(Throwable throwable) {


        log.error("系统繁忙，请稍后再试 :",throwable);
        return new UserClient() {
            @Override
            public UserSimpleInfo getUserInfo(String account, LoginType type) {
                log.info("查询用户信息服务异常");
                return null;
            }


            @Override
            public List<UserSimpleInfo> getUserInfoList(List<Long> userIds) {
                return List.of();
            }

            @Override
            public Result<List<CleanerSimpleVO>> listByRole(String role) {
                return null;
            }


        };

    }
}