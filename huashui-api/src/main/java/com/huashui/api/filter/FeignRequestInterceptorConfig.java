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
            
            if (attributes != null) {
                HttpServletRequest request = attributes.getRequest();
                String userId = request.getHeader("X-User-Id");
                
                if (StringUtils.isNotBlank(userId)) {
                    // 把当前请求的X-User-Id续传到即将发起的Feign请求里
                    template.header("X-User-Id", userId);
                }
            }
        };
    }
}