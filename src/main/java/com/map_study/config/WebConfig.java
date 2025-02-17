package com.map_study.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // 자유게시판 파일 접근
        registry.addResourceHandler("/files/**")
                .addResourceLocations("file:" + System.getProperty("user.dir") + "/upload/");

        // 비밀게시판 파일 접근
        registry.addResourceHandler("/secretfiles/**")
                .addResourceLocations("file:" + System.getProperty("user.dir") + "/secretupload/");
    }
}