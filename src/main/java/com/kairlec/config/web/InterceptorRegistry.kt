package com.kairlec.config.web

import com.kairlec.contrller.admin.AdminInterceptor
import com.kairlec.contrller.file.FileInterceptor
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.servlet.config.annotation.CorsRegistry
import org.springframework.web.servlet.config.annotation.InterceptorRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer

/**
 *@program: SKExplorer
 *@description: 拦截器注册器
 *@author: Kairlec
 *@create: 2020-03-08 18:04
 */
@Configuration
open class InterceptorRegistry : WebMvcConfigurer {
    @Bean
    open fun AdminInterceptorMaker(): AdminInterceptor {
        return AdminInterceptor()
    }

    @Bean
    open fun FileInterceptorMaker(): FileInterceptor {
        return FileInterceptor()
    }

    override fun addInterceptors(registry: InterceptorRegistry) {
        registry.addInterceptor(AdminInterceptorMaker()).addPathPatterns(AdminInterceptor.PathPatterns)
        registry.addInterceptor(FileInterceptorMaker()).addPathPatterns(FileInterceptor.PathPatterns)
    }

    override fun addCorsMappings(registry: CorsRegistry) {
        registry.addMapping("/**")
                .allowCredentials(true)
                .allowedMethods("GET", "POST", "OPTIONS")
                .allowedHeaders("*")
                .exposedHeaders(
                        "access-control-allow-headers",
                        "access-control-allow-methods",
                        "access-control-allow-origin",
                        "access-control-max-age",
                        "X-Frame-Options"
                )
    }
}