package com.huashui.common.filter;

import com.huashui.common.utils.UserContext;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Slf4j
@Component
public class UserInfoFilter implements Filter {

    private static final String USER_ID_HEADER = "X-User-Id";

    private static final String USER_ROLE_HEADER = "X-User-Role";

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain)
            throws IOException, ServletException {

        try {
            HttpServletRequest httpRequest = (HttpServletRequest) request;

            // 获取 Gateway 传递的用户 ID
            String userId = httpRequest.getHeader(USER_ID_HEADER);

            // 获取 Gateway 传递的用户角色
            String role = httpRequest.getHeader(USER_ROLE_HEADER);

            // 设置用户 ID
            if (userId != null && !userId.isBlank()) {

                try {
                    UserContext.setUserId(Long.valueOf(userId));
                } catch (NumberFormatException e) {

                    HttpServletResponse httpResponse = (HttpServletResponse) response;

                    httpResponse.sendError(HttpServletResponse.SC_UNAUTHORIZED, "用户身份信息非法");

                    return;
                }
            }

            // 设置角色
            if (role != null && !role.isBlank()) {
                UserContext.setRole(role);
            }
            // 继续执行过滤器链
            filterChain.doFilter(request, response);

        } finally {
            // 请求结束后清理 ThreadLocal
            UserContext.clean();
        }
    }

}
