package com.huashui.common.config;

import com.huashui.common.filter.UserInfoFilter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnWebApplication
public class UserContextAutoConfiguration {


    @Bean
    public UserInfoFilter userInfoFilter(){
        return new UserInfoFilter();
    }



}