package com.crypto.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.crypto.service.security.AuthentificationInterceptor;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    // private static final String [] excudedPathPatterns = {
    //     "/inscription", "/connection"
    // };

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new AuthentificationInterceptor()).addPathPatterns("/crypto/**"); // Applique à toutes les URLs
    }
}
