package com.huashui.gateway.Filter;

import cn.dev33.satoken.stp.StpUtil;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.Set;

@Component
public class AuthGlobalFilter implements GlobalFilter, Ordered {



    /**
     * 用户 ID Header
     */
    public static final String USER_ID_HEADER = "X-User-Id";

    /**
     * 用户角色 Header
     */
    public static final String USER_ROLE_HEADER = "X-User-Role";

    /**
     * Sa-Token Session 中角色的 Key
     */
    private static final String SESSION_ROLE_KEY = "role";

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {

        /*
         *
         * 这里不需要再次 checkLogin。
         *
         * 因为 SaReactorFilter 已经完成了 Token 鉴权。
         */

        // 获取当前登录用户 ID
        String userId = StpUtil.getLoginIdAsString();

        // 获取当前用户角色
        String role = StpUtil.getSession().getString(SESSION_ROLE_KEY);

        // 理论上正常登录用户一定存在角色
        if (userId == null || userId.isBlank() || role == null || role.isBlank()) {
            return chain.filter(exchange);
        }

        /*
         * 删除客户端可能伪造的 Header，
         * 然后由 Gateway 重新写入可信身份信息。
         */
        ServerHttpRequest request = exchange
                .getRequest()
                .mutate()
                .headers(headers -> {
                    headers.remove(USER_ID_HEADER);
                    headers.remove(USER_ROLE_HEADER);
                    //向请求头保存userid和role
                    headers.set(USER_ID_HEADER, userId);
                    headers.set(USER_ROLE_HEADER, role);
                }).build();

        ServerWebExchange newExchange = exchange
                .mutate()
                .request(request)
                .build();

        return chain.filter(newExchange);
    }
    @Override
    public int getOrder() {
        return 1;
    }
}
