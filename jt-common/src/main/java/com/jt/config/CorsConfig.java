package com.jt.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig implements WebMvcConfigurer {
    //扩展跨域请求的方法
    @Override
    public void addCorsMappings(CorsRegistry registry) {

        //允许什么样的请求进行跨域
        // /*允许一级目录跨域 /** 标识允许多级目录跨域
        registry.addMapping("/**")
        //2 允许哪些服务器跨域
                .allowedOrigins("*")
        //3  是否允许携带cookie
                .allowCredentials(true)
                //4 定义探针时间
                .maxAge(1800);

    }



}
