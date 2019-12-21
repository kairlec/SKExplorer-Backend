package com.kairlec.contrller;

import com.kairlec.local.utils.ResponseDataUtils;
import com.kairlec.local.utils.SKFileUtils;
import com.kairlec.pojo.Captcha;
import com.kairlec.pojo.User;
import com.kairlec.exception.ServiceError;
import com.kairlec.utils.CaptchaMaker;
import com.kairlec.utils.LocalConfig;
import com.kairlec.utils.file.FileUtils;
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
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collection;

@RequestMapping("/admin")
@RestController
public class AdminController {
    private static Logger logger = LogManager.getLogger(AdminController.class);

    @RequestMapping(value = "/captcha")
    public void captcha(HttpSession session, HttpServletResponse response) {
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
    public void newcaptcha(HttpSession session, HttpServletResponse response) {
        Captcha captcha = (Captcha) session.getAttribute("captcha");
        if (captcha == null) {
            response.setStatus(403);
            return;
        }
        session.setAttribute("captcha", CaptchaMaker.getCaptcha(LocalConfig.getConfigBean().getCaptchacount()));
        logger.info("刷新验证码:" + ((Captcha) session.getAttribute("captcha")).getCaptchaString());
    }

    @RequestMapping(value = "/login", produces = "application/json; charset=utf-8")
    public String login(HttpServletRequest request) {
        HttpSession session = request.getSession(true);

        UserChecker userChecker = new UserChecker(request);
        if (!userChecker.check()) {
            logger.info("用户验证失败:" + userChecker.getFailedResult().getMessage());
            return ResponseDataUtils.Error(userChecker.getFailedResult());
        }
        User user = User.getUser(request);
        Integer result = LocalConfig.getUserService().updateLoginInfo(user);
        if (result == null) {
            logger.warn("Update user login info failed");
        }
        logger.info("用户验证成功");
        session.setAttribute("user", user);
        session.setMaxInactiveInterval(60 * 60);
        return ResponseDataUtils.successData(user);
    }

    @RequestMapping(value = "/logout", produces = "application/json; charset=utf-8")
    public String logout(HttpServletRequest request) {
        HttpSession session = request.getSession(true);
        session.invalidate();
        return ResponseDataUtils.OK();
    }

    @RequestMapping(value = "/relogin", produces = "application/json; charset=utf-8")
    public String relogin(HttpSession session) {
        return ResponseDataUtils.successData(session.getAttribute("user"));
    }

    @RequestMapping(value = {"/upload", "/upload/", "/upload/**"})
    public String upload(HttpServletRequest request) {
        logger.info("request.getContentType(): " + request.getContentType());
        if (!request.getContentType().split(";")[0].equals("multipart/form-data")) {
            return ResponseDataUtils.Error(ServiceError.NOT_MULTIPART_FROM_DATA);
        }

        String requestUrl = URLDecoder.decode(request.getRequestURI(), StandardCharsets.UTF_8);
        String currentPath = requestUrl.substring(13);

        logger.info("文件路径：" + LocalConfig.getConfigBean().getContentdir() + currentPath);
        File file = new File(LocalConfig.getConfigBean().getContentdir() + currentPath);
        if (file.exists() && file.isFile()) {
            return ResponseDataUtils.Error(ServiceError.INVALID_DIR);
        }
        if (!file.mkdirs()) {
            return ResponseDataUtils.Error(ServiceError.IO_EXCEPTION);
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
        } catch (IOException | ServletException e) {
            e.printStackTrace();
            return ResponseDataUtils.Error(e);
        }
        return ResponseDataUtils.successData(counts);
    }

    @RequestMapping(value = {"/move", "/move/", "/move/**"})
    public String move(HttpServletRequest request) {
        String path = FileUtils.getPathByRequest(request, "move");

        return ServiceError.UNKNOWN.toString();
    }

    @RequestMapping(value = {"/delete", "/delete/", "/delete/**"})
    public String delete(HttpServletRequest request) {
        String path = FileUtils.getPathByRequest(request, "delete");
        Path filePath = Paths.get(SKFileUtils.getAbsolutePath(path));

        if (!Files.exists(filePath, LinkOption.NOFOLLOW_LINKS)) {
            return ServiceError.FILE_NOT_EXISTS.toString();
        }
        try {
            Files.delete(filePath);
        } catch (IOException e) {
            return ResponseDataUtils.fromException(e).toString();
        }
        return ServiceError.NO_ERROR.toString();
    }

    @RequestMapping(value = "/config")
    public String config(HttpServletRequest request) {
        return ServiceError.UNSPECIFIED.toString();
    }

}
