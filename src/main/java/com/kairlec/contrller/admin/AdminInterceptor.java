package com.kairlec.contrller.admin;

import com.kairlec.local.utils.ResponseDataUtils;
import com.kairlec.local.utils.UserUtils;
import com.kairlec.pojo.User;
import com.kairlec.exception.ServiceError;
import com.kairlec.utils.LocalConfig;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.LinkedList;
import java.util.List;

@Component
public class AdminInterceptor implements HandlerInterceptor {
    private static Logger logger = LogManager.getLogger(AdminController.class);
    private static List<String> blackAPIList = new LinkedList<>() {
        {
            add("/admin/logout");
            add("/admin/login");
            add("/admin/login/key");
            add("/admin/captcha");
            add("/admin/captcha/fresh");
        }
    };

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (!request.getMethod().equalsIgnoreCase("POST")) {
            logger.debug("非POST方法请求拦截域内容,拒绝");
            response.setStatus(403);
            return false;
        }
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");
        String requestUri = URLDecoder.decode(request.getRequestURI(), StandardCharsets.UTF_8);
        HttpSession session = request.getSession();
        logger.info("获取到session,ID=" + session.getId());
        if (blackAPIList.contains(requestUri)) {
            return true;
        }
        logger.debug("有新的拦截域内容请求,URI=" + requestUri);
        ServiceError checkStatus = UserUtils.checkStatus(session);
        if (checkStatus.OK()) {
            return true;
        } else {
            response.getWriter().write(ResponseDataUtils.Error(checkStatus));
            return false;
        }
    }

}