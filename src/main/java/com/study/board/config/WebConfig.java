package com.study.board.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    //view에서 접근할 경로
    private String resourcePath = "/upload/**";
    //실제 파일 저장 경로
    private String savePath = "file:///C://SW_WORKSPACE//Java//board//src//main//resources//images/";

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler(resourcePath).addResourceLocations(savePath);
    }
}
