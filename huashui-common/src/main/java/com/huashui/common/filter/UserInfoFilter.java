package com.huashui.common.filter;

import com.huashui.common.utils.UserContext;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.ComponentScan;
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


        if(userId != null){
            log.info("++++++++common模块拦截用户Id:{}+++++++++++",userId);
            UserContext.setUserId(Long.valueOf(userId));
        }


        try {

            chain.doFilter(request, servletResponse);

        } finally {

            UserContext.remove();

        }
    }
}