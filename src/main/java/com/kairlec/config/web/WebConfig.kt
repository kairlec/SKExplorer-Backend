package com.kairlec.config.web

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.kairlec.contrller.admin.AdminInterceptor
import com.kairlec.contrller.file.FileInterceptor
import com.kairlec.utils.LocalConfig
import org.springframework.beans.factory.config.ConfigurableBeanFactory
import org.springframework.boot.SpringBootConfiguration
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean
import org.springframework.boot.autoconfigure.http.HttpMessageConverters
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Primary
import org.springframework.context.annotation.Scope
import org.springframework.http.converter.HttpMessageConverter
import org.springframework.http.converter.StringHttpMessageConverter
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter
import org.springframework.stereotype.Component
import org.springframework.web.servlet.config.annotation.ContentNegotiationConfigurer
import org.springframework.web.servlet.config.annotation.CorsRegistry
import org.springframework.web.servlet.config.annotation.InterceptorRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer
import java.nio.charset.StandardCharsets
import java.text.DateFormat

/**
 *@program: SKExplorer
 *@description: SpringMVC一些配置
 *@author: Kairlec
 *@create: 2020-03-11 15:44
 */

@SpringBootConfiguration
open class WebConfig : WebMvcConfigurer {

    @Bean(name = ["jacksonObjectMapper"])
    @Primary
    @Scope(ConfigurableBeanFactory.SCOPE_SINGLETON)
    @ConditionalOnMissingBean(ObjectMapper::class)
    open fun jacksonObjectMapper(builder: Jackson2ObjectMapperBuilder): ObjectMapper {
        return LocalConfig.objectMapper
    }

    @Bean
    @Primary
    @ConditionalOnBean(ObjectMapper::class)
    open fun jacksonConverters(objectMapper: ObjectMapper): HttpMessageConverters {
        val jackson2HttpMessageConverter = MappingJackson2HttpMessageConverter()
        jackson2HttpMessageConverter.objectMapper = objectMapper
        return HttpMessageConverters(jackson2HttpMessageConverter)
    }


    /**
     * 设置响应的字符编码
     * @return HttpMessageConverter
     */
    @Bean
    open fun responseBodyConverter(): HttpMessageConverter<String> {
        return StringHttpMessageConverter(StandardCharsets.UTF_8)
    }

    /**
     * 添加管理员API拦截器
     * @return 管理员API链接器
     */
    @Bean
    open fun adminInterceptorMaker(): AdminInterceptor {
        return AdminInterceptor()
    }

    /**
     * 添加文件管理API拦截器
     * @return 文件管理API链接器
     */
    @Bean
    open fun fileInterceptorMaker(): FileInterceptor {
        return FileInterceptor()
    }

    override fun configureMessageConverters(converters: MutableList<HttpMessageConverter<*>>) {
        super.configureMessageConverters(converters)
        converters.add(responseBodyConverter())
    }

    override fun configureContentNegotiation(configurer: ContentNegotiationConfigurer) {
        configurer.favorPathExtension(false) // 支持后缀匹配
    }

    override fun addInterceptors(registry: InterceptorRegistry) {
        registry.addInterceptor(adminInterceptorMaker()).addPathPatterns(AdminInterceptor.PathPatterns)
        registry.addInterceptor(fileInterceptorMaker()).addPathPatterns(FileInterceptor.PathPatterns)
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