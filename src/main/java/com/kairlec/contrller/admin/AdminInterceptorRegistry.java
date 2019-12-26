package com.kairlec.contrller.admin;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Configuration
public class AdminInterceptorRegistry implements WebMvcConfigurer {

    @Bean
    AdminInterceptor AdminControllerInterceptor() {
        return new AdminInterceptor();
    }

    List<String> PathPatterns = new ArrayList<>(Arrays.asList(
            "/admin",
            "/admin/*"
    ));

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(AdminControllerInterceptor()).addPathPatterns(PathPatterns);
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/*").allowCredentials(true);
    }

}