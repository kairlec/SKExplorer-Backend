package com.kairlec.local.utils;

import com.kairlec.exception.ServiceError;
import com.kairlec.pojo.Captcha;
import com.kairlec.pojo.User;
import com.kairlec.utils.CaptchaMaker;
import com.kairlec.utils.LocalConfig;
import com.kairlec.utils.Network;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.time.LocalDateTime;

public abstract class UserUtils {
    private static Logger logger = LogManager.getLogger(UserUtils.class);

    private UserUtils() {
        throw new AssertionError("工具类不允许实例化");
    }

    public static void updateLoginInfo(User user, HttpServletRequest request) {
        user.setLastLoginTime(LocalDateTime.now());
        user.setLastSessionId(request.getSession().getId());
    }

    public static void updateLoginInfo(User user, HttpSession session) {
        user.setLastLoginTime(LocalDateTime.now());
        user.setLastSessionId(session.getId());
    }

    public static ServiceError checkStatus(HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            logger.info("新打开session或未找到已有用户");
            logger.info("无法处理的未登录请求");
            return ServiceError.NOT_LOGGED_IN;
        } else {
            logger.info("在Session取到了'" + user.getUsername() + "'的用户名");
            User targetUser = LocalConfig.getUserService().getUser(user.getUsername());
            if (!targetUser.getLastSessionId().equals(session.getId())) {
                session.invalidate();
                return ServiceError.EXPIRED_LOGIN;
            }
            UserUtils.updateLoginInfo(targetUser, session);
            session.setAttribute("user", targetUser);
            session.setMaxInactiveInterval(60 * 60);
            return ServiceError.NO_ERROR;
        }
    }

    public static ServiceError checkLogin(HttpServletRequest request) {
        HttpSession session = request.getSession();
        if (session.getAttribute("user") != null) {
            return ServiceError.HAD_LOGGED_IN;//已经登录过
        }
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        String IP = Network.getIpAddress(request);
        if (username == null) {
            return ServiceError.NULL_USERNAME;//空的用户名
        }
        if (password == null) {
            return ServiceError.NULL_PASSWORD;//空的密码
        }
        User user = LocalConfig.getUserService().getUser(username);
        if (user == null) {
            return ServiceError.USERNAME_NOT_EXISTS;//错误的用户名
        }
        Captcha captcha = (Captcha) session.getAttribute("captcha");
        if (captcha != null) {
            String captchaString = request.getParameter("captcha");
            if (captchaString == null || !captcha.check(captchaString)) {
                return ServiceError.WRONG_CAPTCHA;//错误的验证码
            }
            session.removeAttribute("captcha");
        } else {
            if (IP != null && user.getIP() != null && !IP.equals(user.getIP())) {
                session.setAttribute("captcha", CaptchaMaker.getCaptcha(LocalConfig.getConfigBean().getCaptchacount()));
                return ServiceError.NEED_VERIFY;//不受信任的IP,需要验证
            }
        }
        password = password.replace(' ', '+');
        try {
            password = PasswordCoderUtils.fromRequest(password);
        } catch (Exception e) {
            return ServiceError.UNKNOWN_PASSWORD;//未知的密码串
        }
        logger.info("解密密码得:" + password);
        logger.info("数据库的密码:" + user.getPassword());
        if (!user.equalsPassword(password)) {
            session.setAttribute("captcha", CaptchaMaker.getCaptcha(LocalConfig.getConfigBean().getCaptchacount()));
            return ServiceError.WRONG_PASSWORD;//错误的密码
        }
        return ServiceError.NO_ERROR;
    }

}
