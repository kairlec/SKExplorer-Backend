package com.kairlec.utils.token;

import com.kairlec.pojo.Captcha;
import com.kairlec.pojo.User;
import com.kairlec.exception.ErrorCodeClass;
import com.kairlec.utils.*;
import lombok.*;
import com.kairlec.exception.ErrorCode;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Date;

@Data
@NoArgsConstructor
public class UserChecker {
    @Getter(AccessLevel.NONE)
    @Setter(AccessLevel.NONE)
    private static final Logger logger = LogManager.getLogger(UserChecker.class);

    private String username;
    private String password;
    private String IP;
    private Date lastLoginTime;
    private HttpServletRequest request;

    @Setter(AccessLevel.NONE)
    private ErrorCode failedResult;


    public UserChecker(HttpServletRequest request) {
        this.request = request;
        this.username = request.getParameter("username");
        this.password = request.getParameter("password");
        this.IP = Network.getIpAddress(request);
    }

    public boolean check() {
        HttpSession session = request.getSession(true);
        if (session.getAttribute("user") != null) {
            failedResult = ErrorCodeClass.HAD_LOGGED_IN;//已经登录过
            return false;
        }
        if (username == null) {
            failedResult = ErrorCodeClass.NULL_USERNAME;//空的用户名
            return false;
        }
        if (password == null) {
            failedResult = ErrorCodeClass.NULL_PASSWORD;//空的密码
            return false;
        }
        User user = LocalConfig.getUserService().getUser(username);
        if (user == null) {
            failedResult = ErrorCodeClass.USERNAME_NOT_EXISTS;//错误的用户名
            return false;
        }
        Captcha captcha = (Captcha) session.getAttribute("captcha");
        if (captcha != null) {
            String captchaString = request.getParameter("captcha");
            if (captchaString == null) {
                failedResult = ErrorCodeClass.NULL_CAPTCHA;//需要验证但是验证码为空
                return false;
            }
            if (!captcha.check(captchaString)) {
                failedResult = ErrorCodeClass.WRONG_CAPTCHA;//错误的验证码
                return false;
            }
            session.removeAttribute("captcha");
            logger.info("验证码验证成功,移除验证码请求");
        } else {
            if (IP != null && user.getIP() != null && !IP.equals(user.getIP())) {
                logger.info("不受信任的IP,需要验证登录");
                session.setAttribute("captcha", CaptchaMaker.getCaptcha(LocalConfig.getConfigBean().getCaptchacount()));
                failedResult = ErrorCodeClass.NEED_VERIFY;//不受信任的IP,需要验证
                return false;
            }
        }
        logger.info("Password=" + password);
        password = password.replace(' ', '+');
        try {
            password = PasswordCoder.fromRequest(password);
        } catch (Exception e) {
            failedResult = ErrorCodeClass.UNKNOWN_PASSWORD;//未知的密码串
            return false;
        }
        logger.info("解密的密码:" + password);
        logger.info("数据库得到的密码:" + user.getPassword());
        if (!user.getPassword().equals(password)) {
            session.setAttribute("captcha", CaptchaMaker.getCaptcha(LocalConfig.getConfigBean().getCaptchacount()));
            failedResult = ErrorCodeClass.WRONG_PASSWORD;//错误的密码
            return false;
        }
        return true;
    }

}
