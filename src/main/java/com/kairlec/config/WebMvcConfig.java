package com.kairlec.config;

import com.kairlec.contrller.AdminControllerInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    /**
     * 自己定义的拦截器类
     *
     * @return
     */
    @Bean
    AdminControllerInterceptor AdminControllerInterceptor() {
        return new AdminControllerInterceptor();
    }

    List<String> PathPatterns = new ArrayList<>(Arrays.asList(
            "/admin",
            "/admin/*",
            "/submit",
            "/submit/*",
            "/request",
            "/request/*"
    ));

    /**
     * 添加拦截器
     *
     * @param registry
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(AdminControllerInterceptor()).addPathPatterns(PathPatterns);
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/*").allowCredentials(true);
    }

}