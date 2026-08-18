package com.huashui.gateway.config;

import cn.dev33.satoken.reactor.context.SaReactorSyncHolder;
import cn.dev33.satoken.reactor.filter.SaReactorFilter;
import cn.dev33.satoken.stp.StpUtil;
import com.huashui.gateway.Filter.AuthGlobalFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.server.ServerWebExchange;

@Configuration
public class SaTokenReactorConfig {

    @Bean
    public SaReactorFilter saReactorFilter() {
        return new SaReactorFilter()
                .addInclude("/**")
                .addExclude(
                        "/auth/login",
                        "/auth/logout",
                        "/auth/captcha",
                        "/auth/email/login",
                        "/auth/bind/email/send-code",
                        "/auth/register",
                        "/notice/latest",
                        "/doc.html",
                        "/webjars/**",
                        "/v3/api-docs/**",
                        "/swagger-resources/**",
                        "/favicon.ico"
                )
                .setBeforeAuth(obj -> {
                    try {
                        ServerWebExchange exchange = SaReactorSyncHolder.getExchange();
                        if (exchange != null && StpUtil.isLogin()) {
                            exchange.getAttributes().put(AuthGlobalFilter.USER_ID_HEADER, StpUtil.getLoginIdAsString());
                            exchange.getAttributes().put(AuthGlobalFilter.USER_ROLE_HEADER, StpUtil.getSession().getString("role"));
                        }
                    } catch (Exception ignored) {
                        // 未登录或 token 无效时由后续 SaReactorFilter 统一处理
                    }
                })
                .setAuth(obj -> StpUtil.checkLogin());
    }
}