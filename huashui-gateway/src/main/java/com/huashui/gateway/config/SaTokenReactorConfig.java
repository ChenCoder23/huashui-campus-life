package com.huashui.gateway.config;

import cn.dev33.satoken.reactor.filter.SaReactorFilter;
import cn.dev33.satoken.stp.StpUtil;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Sa-Token Reactor 配置 (Spring Cloud Gateway / WebFlux)
 *
 * <p>⚠️ 关键：WebFlux 模式下不会自动注册 SaReactorFilter，
 * 必须显式注册此 Bean，否则所有鉴权请求返回 401。</p>
 */
@Configuration
public class SaTokenReactorConfig {

    @Bean
    public SaReactorFilter saReactorFilter() {
        return new SaReactorFilter()
                // 拦截所有路径
                .addInclude("/**")
                // 放行白名单
                .addExclude(
                        "/auth/login",
                        "/auth/logout",
                        "/auth/captcha",
                        "/auth/register",
                        "/doc.html",
                        "/webjars/**",
                        "/v3/api-docs/**",
                        "/swagger-resources/**",
                        "/favicon.ico"
                )
                // 认证规则：未登录拒绝
                .setAuth(obj -> StpUtil.checkLogin());
    }
}
