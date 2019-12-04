package com.kairlec.contrller;

import com.kairlec.pojo.User;
import com.kairlec.exception.ErrorCodeClass;
import com.kairlec.utils.LocalConfig;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.LinkedList;
import java.util.List;

@Component
public class AdminControllerInterceptor implements HandlerInterceptor {
    private static Logger logger = LogManager.getLogger(AdminController.class);
    private static List<String> blackAPIList = new LinkedList<>() {
        {
            add("/admin/logout");
            add("/admin/login");
            add("/admin/captcha");
            add("/admin/newcaptcha");
            add("/error/post");
        }
    };

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // TODO Auto-generated method stub
        if (!request.getMethod().equalsIgnoreCase("POST")) {
            response.setStatus(403);
            return false;
        }
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");
        String requestUrl = URLDecoder.decode(request.getRequestURI(), StandardCharsets.UTF_8);

        HttpSession session = request.getSession(true);
        logger.info("获取到session,ID=" + session.getId());
        if (blackAPIList.contains(requestUrl)) {
            return true;
        }
        User user = (User) session.getAttribute("user");
        if (user == null) {
            logger.info("新打开session或未找到已有用户");
            logger.info("无法处理的未登录请求");
            response.getWriter().write(ErrorCodeClass.NOT_LOGGED_IN.toString());
            return false;
        } else {
            logger.info("在Session取到了'" + user.getUsername() + "'的用户名");
            User targetUser = LocalConfig.getUserService().getUser(user.getUsername());
            if (!targetUser.getLastSessionId().equals(session.getId())) {
                session.invalidate();
                response.getWriter().write(ErrorCodeClass.EXPIRED_LOGIN.toString());//已登录状态与上一次的登录SessionID不一致,表示在其他地方登录,老的被挤下线
                return false;
            }
            session.setAttribute("user", targetUser);
            session.setMaxInactiveInterval(60 * 60);
            return true;
        }
    }


    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        // TODO Auto-generated method stub
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
            throws Exception {
        // TODO Auto-generated method stub
    }
}