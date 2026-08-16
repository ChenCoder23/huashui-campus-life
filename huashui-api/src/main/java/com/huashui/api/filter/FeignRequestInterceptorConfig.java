package com.huashui.api.filter;

import feign.RequestInterceptor;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Configuration
public class FeignRequestInterceptorConfig {

    @Bean
    public RequestInterceptor requestInterceptor() {
        return template -> {
            // 拿到当前正在处理的HTTP请求（即user服务收到的原始请求）
            ServletRequestAttributes attributes =
                (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();

            if (attributes == null) {
                return;
            }

            //获取request
            HttpServletRequest request = attributes.getRequest();


            String userId = request.getHeader("X-User-Id");

            String role = request.getHeader("X-User-Role");

            if (userId != null && !userId.isBlank()) {
                //将userid放入请求头
                template.header("X-User-Id", userId);
            }

            if (role != null && !role.isBlank()) {
                //将roled放入请求头
                template.header("X-User-Role", role);
            }

        };
    }
}