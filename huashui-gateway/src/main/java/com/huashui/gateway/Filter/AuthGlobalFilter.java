package com.huashui.gateway.Filter;

import cn.dev33.satoken.stp.StpUtil;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.Set;

@Component
public class AuthGlobalFilter implements GlobalFilter, Ordered {

    private static final Set<String> AUTH_WHITELIST = Set.of(
        "/auth/login", "/auth/captcha", "/auth/logout",
        "/auth/bind", "/auth/email",
        "/doc.html", "/webjars", "/v3/api-docs", "/swagger-resources", "/favicon.ico"
    );

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        String path = exchange.getRequest().getPath().value();

        // 白名单：不碰 SaToken，直接放行
        if (isWhitelist(path)) {
            return chain.filter(exchange);
        }

        // 已认证路径：注入用户信息到 Header
        ServerHttpRequest request = exchange.getRequest().mutate()
            .header("X-User-Id", StpUtil.getLoginIdAsString())
            .header("X-User-Roles", (String) StpUtil.getSession().get("role"))
            .build();

        return chain.filter(exchange.mutate().request(request).build());
    }

    private boolean isWhitelist(String path) {
        for (String w : AUTH_WHITELIST) {
            if (path.startsWith(w)) return true;
        }
        return false;
    }

    @Override
    public int getOrder() {
        return 1;
    }
}
