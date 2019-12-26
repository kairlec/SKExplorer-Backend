package com.kairlec.contrller.admin;

import com.kairlec.local.utils.ResponseDataUtils;
import com.kairlec.local.utils.UserUtils;
import com.kairlec.pojo.Captcha;
import com.kairlec.pojo.User;
import com.kairlec.exception.ServiceError;
import com.kairlec.utils.CaptchaMaker;
import com.kairlec.utils.LocalConfig;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.OutputStream;

@RequestMapping("/admin")
@RestController
public class AdminController {
    private static Logger logger = LogManager.getLogger(AdminController.class);

    @RequestMapping(value = "/captcha")
    void captcha(HttpSession session, HttpServletResponse response) {
        Captcha captcha = (Captcha) session.getAttribute("captcha");
        if (captcha == null) {
            logger.info("当前无需验证,错误的验证码请求");
            response.setStatus(403);
            return;
        }
        logger.info("请求验证码:" + captcha.getCaptchaString());
        response.setDateHeader("Expires", 0);
        response.setHeader("Cache-Control", "no-store, no-cache, must-revalidate");
        response.addHeader("Cache-Control", "post-check=0, pre-check=0");
        response.setHeader("Pragma", "no-cache");
        response.setContentType("image/jpeg");
        logger.info("输出验证码到流");
        try (
                OutputStream outputStream = response.getOutputStream();
        ) {
            captcha.outputStream(outputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @RequestMapping(value = "/captcha/fresh")
    void freshCaptcha(HttpSession session, HttpServletResponse response) {
        Captcha captcha = (Captcha) session.getAttribute("captcha");
        if (captcha == null) {
            response.setStatus(403);
            return;
        }
        session.setAttribute("captcha", CaptchaMaker.getCaptcha(LocalConfig.getConfigBean().getCaptchacount()));
        logger.info("刷新验证码:" + ((Captcha) session.getAttribute("captcha")).getCaptchaString());
    }

    @RequestMapping(value = "/login")
    String login(HttpSession session, HttpServletRequest request) {
        ServiceError checkLogin = UserUtils.checkLogin(request);
        if (!checkLogin.equals(ServiceError.NO_ERROR)) {
            return ResponseDataUtils.Error(checkLogin);
        }
        User user = LocalConfig.getUserService().getUser(request.getParameter("username"));
        UserUtils.updateLoginInfo(user, request);
        Integer result = LocalConfig.getUserService().updateLoginInfo(user);
        if (result == null) {
            logger.warn("Update user login info failed");
        }
        logger.info("用户验证成功");
        session.setAttribute("user", user);
        session.setMaxInactiveInterval(60 * 60);
        return ResponseDataUtils.successData(user);
    }

    @RequestMapping(value = "/login/key")
    String key() {
        return ResponseDataUtils.successData(LocalConfig.getPublicKey());
    }

    @RequestMapping(value = "/logout")
    String logout(HttpServletRequest request) {
        HttpSession session = request.getSession();
        session.invalidate();
        return ResponseDataUtils.OK();
    }

    @RequestMapping(value = "/status")
    String status(HttpSession session) {
        return ResponseDataUtils.successData(session.getAttribute("user"));
    }

    @RequestMapping(value = "/config")
    public String config(HttpServletRequest request) {
        return ServiceError.UNSPECIFIED.toString();
    }

}
