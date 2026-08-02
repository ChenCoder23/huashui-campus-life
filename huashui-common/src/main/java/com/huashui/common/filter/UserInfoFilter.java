package com.huashui.common.filter;

import com.huashui.common.utils.UserContext;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Slf4j
@Component
public class UserInfoFilter implements Filter {

    @Override
    public void doFilter(
            ServletRequest servletRequest,
            ServletResponse servletResponse,
            FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest request = (HttpServletRequest) servletRequest;

        String userId = request.getHeader("X-User-Id");
        String roles = request.getHeader("X-User-Roles");

        if (userId != null) {
            UserContext.setUserId(Long.valueOf(userId));
        }
        if (roles != null) {
            UserContext.setRoles(roles);
        }

        try {
            chain.doFilter(request, servletResponse);
        } finally {
            UserContext.remove();
        }
    }
}
