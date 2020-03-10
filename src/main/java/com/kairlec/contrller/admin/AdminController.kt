package com.kairlec.contrller.admin

import com.kairlec.pojo.Captcha
import com.kairlec.config.startup.StartupConfig
import com.kairlec.exception.ServiceErrorEnum
import com.kairlec.local.utils.ResponseDataUtils
import com.kairlec.local.utils.UserUtils
import com.kairlec.utils.LocalConfig
import org.apache.logging.log4j.LogManager
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse
import javax.servlet.http.HttpSession

/**
 *@program: SKExplorer
 *@description: 管理员接口
 *@author: Kairlec
 *@create: 2020-03-08 18:07
 */

@RequestMapping("/admin")
@RestController
class AdminController {
    @RequestMapping(value = ["/captcha"])
    fun captcha(session: HttpSession, response: HttpServletResponse): String {
        val captcha = session.getAttribute("captcha")
        if (captcha is Captcha) {
            return ResponseDataUtils.ok(captcha.skImage.toBase64())
        }
        response.status = 403
        ServiceErrorEnum.UNKNOWN_REQUEST.throwout()
    }

    @RequestMapping(value = ["/captcha/fresh"])
    fun freshCaptcha(session: HttpSession, response: HttpServletResponse) {
        val captcha = session.getAttribute("captcha")
        if (captcha is Captcha) {
            session.setAttribute("captcha", Captcha.getInstant(StartupConfig.captchaCount))
        }
        response.status = 403
    }

    @RequestMapping(value = ["/login"])
    fun login(session: HttpSession, request: HttpServletRequest): String {
        val user = UserUtils.authLogin(request)
        logger.info("用户验证成功")
        session.setAttribute("user", user)
        session.maxInactiveInterval = 60 * 60
        return ResponseDataUtils.ok(user)
    }

    @RequestMapping(value = ["/login/key"])
    fun key(): String {
        return ResponseDataUtils.ok(StartupConfig.publicKey)
    }

    @RequestMapping(value = ["/logout"])
    fun logout(session: HttpSession): String {
        session.invalidate()
        return ResponseDataUtils.ok()
    }

    @RequestMapping(value = ["/status"])
    fun status(session: HttpSession): String {
        return ResponseDataUtils.ok(session.getAttribute("user"))
    }

    @RequestMapping(value = ["/config"])
    fun config(request: HttpServletRequest?): String {
        return ServiceErrorEnum.UNSPECIFIED.toString()
    }

    companion object {
        private val logger = LogManager.getLogger(AdminController::class.java)
    }
}
