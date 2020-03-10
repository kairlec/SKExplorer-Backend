package com.kairlec.local.utils

import com.kairlec.pojo.Captcha
import com.kairlec.config.startup.StartupConfig
import com.kairlec.contrller.admin.AdminController
import com.kairlec.exception.ServiceErrorEnum
import com.kairlec.local.utils.PasswordCoderUtils.fromRequest
import com.kairlec.pojo.User
import com.kairlec.utils.LocalConfig
import com.kairlec.utils.Network
import org.apache.logging.log4j.LogManager
import java.net.URLDecoder
import java.nio.charset.StandardCharsets
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpSession


object UserUtils {
    private val logger = LogManager.getLogger(UserUtils::class.java)

    fun authHttpSession(session: HttpSession): ServiceErrorEnum {
        (session.getAttribute("user") as? User)?.let { user ->
            logger.info("""在Session取到了"${user.username}"的用户名""")
            return when {
                //ID不一样,在其他地方登录,挤下线
                user.lastSessionId != session.id -> {
                    session.invalidate()
                    ServiceErrorEnum.EXPIRED_LOGIN
                }
                else -> {
                    ServiceErrorEnum.NO_ERROR
                }
            }
        }
        return ServiceErrorEnum.NOT_LOGGED_IN

    }

    fun authHttpServletRequest(request: HttpServletRequest, blackAPIList: Array<String>): ServiceErrorEnum {
        val requestUrl = URLDecoder.decode(request.requestURI, StandardCharsets.UTF_8)
        if (requestUrl in blackAPIList) {
            return ServiceErrorEnum.NO_ERROR.data("BlackList")
        }
        if (!request.method.equals("POST", ignoreCase = true)) {
            return ServiceErrorEnum.UNKNOWN_REQUEST
        }
        val session = request.getSession(true)
        return authHttpSession(session)
    }

    fun authLogin(request: HttpServletRequest): User {
        val session = request.session
        if (session.getAttribute("user") != null) {
            ServiceErrorEnum.HAD_LOGGED_IN.throwout() //已经登录过
        }
        val username = request.getParameter("username")
        var password = request.getParameter("password")
        val ip = Network.getIpAddress(request)
        if (username == null) {
            ServiceErrorEnum.NULL_USERNAME.throwout() //空的用户名
        }
        if (password == null) {
            ServiceErrorEnum.NULL_PASSWORD.throwout() //空的密码
        }
        val user = LocalConfig.userService.getUser(username)
                ?: ServiceErrorEnum.USERNAME_NOT_EXISTS.throwout() //错误的用户名
        val captcha = session.getAttribute("captcha")
        if (captcha is Captcha) {
            val captchaString = request.getParameter("captcha")
            if (captchaString == null || !captcha.check(captchaString)) {
                ServiceErrorEnum.WRONG_CAPTCHA.throwout() //错误的验证码
            }
            session.removeAttribute("captcha")
        } else {
            if (ip != user.IP) {
                session.setAttribute("captcha", Captcha.getInstant(StartupConfig.captchaCount))
                ServiceErrorEnum.NEED_VERIFY.throwout() //不受信任的IP,需要验证
            }
        }
        password = password.replace(' ', '+')
        password = fromRequest(password)
        if (!user.equalsPassword(password)) {
            session.setAttribute("captcha", Captcha.getInstant(StartupConfig.captchaCount))
            ServiceErrorEnum.WRONG_PASSWORD.throwout() //错误的密码
        }
        user.IP = ip
        user.lastSessionId = session.id
        if (LocalConfig.userService.updateLoginInfo(user) == null) {
            logger.warn("Update user login info failed")
        }
        return user
    }
}
