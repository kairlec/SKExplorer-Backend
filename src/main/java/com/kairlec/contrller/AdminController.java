package com.kairlec.contrller;

import com.kairlec.pojo.Captcha;
import com.kairlec.pojo.User;
import com.kairlec.exception.ErrorCodeClass;
import com.kairlec.utils.CaptchaMaker;
import com.kairlec.utils.LocalConfig;
import com.kairlec.utils.file.SaveFile;
import com.kairlec.utils.token.UserChecker;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.http.Part;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Collection;

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

    @RequestMapping(value = "/newcaptcha")
    void newcaptcha(HttpSession session, HttpServletResponse response) {
        Captcha captcha = (Captcha) session.getAttribute("captcha");
        if (captcha == null) {
            response.setStatus(403);
            return;
        }
        session.setAttribute("captcha", CaptchaMaker.getCaptcha(LocalConfig.getConfigBean().getCaptchacount()));
        logger.info("刷新验证码:" + ((Captcha) session.getAttribute("captcha")).getCaptchaString());
    }

    @RequestMapping(value = "/rpwd", produces = "application/json; charset=utf-8")
    String home(@RequestParam String username, @RequestParam String password) {
        User oldUser = LocalConfig.getUserService().getUser(username);
        if (oldUser == null) {
            return ErrorCodeClass.USERNAME_NOT_EXISTS.toString();
        }
        Integer result;
        User user = new User(username, password);
        result = LocalConfig.getUserService().updatePassword(user);
        if (result == null) {
            return ErrorCodeClass.UNSPECIFIED.toString();
        } else {
            return ErrorCodeClass.NO_ERROR.toString();
        }
    }

    @RequestMapping(value = "/login", produces = "application/json; charset=utf-8")
    public String login(HttpServletRequest request) {
        HttpSession session = request.getSession(true);

        UserChecker userChecker = new UserChecker(request);
        if (!userChecker.check()) {
            logger.info("用户验证失败:" + userChecker.getFailedResult().toString());
            return userChecker.getFailedResult().toString();
        }
        User user = User.getUser(request);
        Integer result = LocalConfig.getUserService().updateLoginInfo(user);
        if (result == null) {
            logger.warn("Update user login info failed");
        }
        logger.info("用户验证成功");
        session.setAttribute("user", user);
        session.setMaxInactiveInterval(60 * 60);
        return ErrorCodeClass.successData(user).toString();
    }

    @RequestMapping(value = "/logout", produces = "application/json; charset=utf-8")
    public String logout(HttpServletRequest request) {
        HttpSession session = request.getSession(true);
        session.invalidate();
        return ErrorCodeClass.NO_ERROR.toString();
    }

    @RequestMapping(value = "/relogin", produces = "application/json; charset=utf-8")
    public String relogin(HttpSession session) {
        return ErrorCodeClass.successData(session.getAttribute("user")).toString();
    }


    @RequestMapping(value = "/upload")
    public String upload(HttpServletRequest request) {
        logger.info("request.getContentType(): " + request.getContentType());

        if (!request.getContentType().split(";")[0].equals("multipart/form-data")) {
            return ErrorCodeClass.NOT_MULTIPART_FROM_DATA.toString();
        }

        String requestUrl = URLDecoder.decode(request.getRequestURI(), StandardCharsets.UTF_8);
        String currentPath = requestUrl.substring(13);

        logger.info("文件路径：" + LocalConfig.getConfigBean().getContentdir() + currentPath);
        File file = new File(LocalConfig.getConfigBean().getContentdir() + currentPath);
        if (file.exists() && file.isFile()) {
            return ErrorCodeClass.INVALID_DIR.toString();
        }
        if (!file.mkdirs()) {
            return ErrorCodeClass.IO_EXCEPTION.toString();
        }
        int counts = 0;
        try {
            Collection<Part> parts = request.getParts();
            logger.info(parts);
            for (Part part : parts) {
                logger.info(part);
                SaveFile.FileProcess(part, LocalConfig.getConfigBean().getContentdir() + currentPath);
                counts++;
            }
        } catch (IOException e) {
            e.printStackTrace();
            return ErrorCodeClass.IO_EXCEPTION.toString();
        } catch (ServletException e) {
            e.printStackTrace();
            return ErrorCodeClass.UNKNOWN_REQUEST.toString();
        }
        return ErrorCodeClass.successData(counts).toString();
    }

}
