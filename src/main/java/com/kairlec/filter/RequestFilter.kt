package com.kairlec.filter

import com.kairlec.model.bo.HTTPInfo
import org.apache.catalina.filters.RemoteIpFilter
import org.apache.logging.log4j.Level
import org.apache.logging.log4j.LogManager
import org.springframework.boot.SpringBootConfiguration
import org.springframework.boot.web.servlet.FilterRegistrationBean
import org.springframework.context.annotation.Bean
import javax.servlet.*
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

/**
 * @program: SKExplorer
 * @description: 过滤器配置
 * @author: Kairlec
 * @create: 2020-03-08 18:05
 * @suppress
 */
@SpringBootConfiguration
open class RequestFilter {
    @Bean
    open fun remoteIpFilter(): RemoteIpFilter {
        return RemoteIpFilter()
    }

    @Bean
    open fun requestFilterRegistration(): FilterRegistrationBean<RequestLogFilter> {
        val registration = FilterRegistrationBean<RequestLogFilter>()
        registration.filter = RequestLogFilter()
        registration.addUrlPatterns("/*")
        registration.order = 1
        return registration
    }

    // 定义过滤器
    inner class RequestLogFilter : Filter {
        override fun doFilter(servletRequest: ServletRequest, servletResponse: ServletResponse, filterChain: FilterChain) {
            servletRequest.characterEncoding = "UTF-8"
            servletResponse.characterEncoding = "UTF-8"
            val response = servletResponse as HttpServletResponse
            val originHeader = (servletRequest as HttpServletRequest).getHeader("Origin")
            response.setHeader("Access-Control-Allow-Origin", originHeader)
            response.setHeader("Access-Control-Max-Age", "86400")
            response.setHeader("Access-Control-Allow-Credentials", "true")
            response.setHeader("Access-Control-Allow-Methods", "GET,POST,OPTIONS")
            response.setHeader("Access-Control-Allow-Headers", "Access-Control,x-ijt")
            response.setHeader("Cache-Control", "no-cache")
            filterChain.doFilter(servletRequest, servletResponse)
            logger.log(Level.getLevel("REQUEST"), HTTPInfo(servletRequest, response).json)
        }
    }

    companion object {
        private val logger = LogManager.getLogger(RequestFilter::class.java)
    }
}