package com.kairlec.config;

import com.kairlec.pojo.Json.HTTPInfo;
import org.apache.catalina.filters.RemoteIpFilter;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Configuration
public class WebConfig {
    private static Logger logger = LogManager.getLogger(WebConfig.class);

    @Bean
    public RemoteIpFilter remoteIpFilter() {
        return new RemoteIpFilter();
    }

    /**
     * 注册第三方过滤器
     * 功能与spring mvc中通过配置web.xml相同
     * 可以添加过滤器锁拦截的 URL，拦截更加精准灵活
     *
     * @return
     */
    @Bean
    public FilterRegistrationBean<AllDomainFilter> testFilterRegistration() {
        FilterRegistrationBean<AllDomainFilter> registration = new FilterRegistrationBean<>();
        registration.setFilter(new AllDomainFilter());
        // 过滤应用程序中所有资源,当前应用程序根下的所有文件包括多级子目录下的所有文件，注意这里*前有“/”
        registration.addUrlPatterns("/*");
        // 过滤器顺序
        registration.setOrder(1);
        return registration;
    }

    // 定义过滤器
    public class AllDomainFilter implements Filter {
        @Override
        public void init(FilterConfig filterConfig) throws ServletException {
        }

        @Override
        public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
            servletRequest.setCharacterEncoding("UTF-8");
            servletResponse.setCharacterEncoding("UTF-8");
            HttpServletResponse response = (HttpServletResponse) servletResponse;
            String originHeader = ((HttpServletRequest) servletRequest).getHeader("Origin");
//            logger.debug("获取到域:" + originHeader);
//            if (LocalConfig.getAllowedOrigins().contains(originHeader)) {
//                logger.debug("允许的域名,设置originHeader");
//                response.setHeader("Access-Control-Allow-Origin", originHeader);
//            } else {
//                logger.debug("不允许的域名");
//                response.setHeader("Access-Control-Allow-Origin", "*");
//            }
            response.setHeader("Access-Control-Allow-Origin", originHeader);
            response.setHeader("Access-Control-Allow-Credentials", "true");
            response.setHeader("Cache-Control", "no-cache");
            filterChain.doFilter(servletRequest, servletResponse);
            logger.log(Level.getLevel("REQUEST"), new HTTPInfo((HttpServletRequest) servletRequest, response).toString());
        }

        @Override
        public void destroy() {
        }
    }
}