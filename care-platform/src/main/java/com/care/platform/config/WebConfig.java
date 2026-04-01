package com.care.platform.config;

import com.care.platform.interceptor.JwtInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.annotation.Resource;

/**
 * Spring Web 核心配置类
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Resource
    private JwtInterceptor jwtInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(jwtInterceptor)
                .addPathPatterns("/**")
                .excludePathPatterns(
                        "/user/login",     // 🌟 必须放行登录接口
                        "/timeslot/list",  // 🌟 建议放行首页列表
                        "/error"           // 🌟 放行 SpringBoot 默认错误页，防止二次报错
                );
    }
}