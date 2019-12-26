package com.kairlec.contrller.file;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Configuration
public class FileInterceptorRegistry implements WebMvcConfigurer {

    @Bean
    FileInterceptor FileControllerInterceptor() {
        return new FileInterceptor();
    }

    List<String> PathPatterns = new ArrayList<>(Arrays.asList(
            "/file",
            "/file/*"
    ));

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(FileControllerInterceptor()).addPathPatterns(PathPatterns);
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/*").allowCredentials(true);
    }

}