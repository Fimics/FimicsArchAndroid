package com.example.as.api.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CommonConfig implements WebMvcConfigurer {
    @Autowired
    private LoginInterceptor loginInterceptor;
    @Autowired
    private CommonInterceptor commonInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(loginInterceptor);
        registry.addInterceptor(commonInterceptor);
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        //添加CORS跨域的支持，https://stackoverflow.com/questions/35091524/spring-cors-no-access-control-allow-origin-header-is-present
        registry.addMapping("/**")
                //允许所有的请求方法
                .allowedMethods("*");
    }
}
