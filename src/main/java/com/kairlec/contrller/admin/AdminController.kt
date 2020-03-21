package com.kairlec.contrller.admin

import com.kairlec.annotation.JsonRequestMapping
import com.kairlec.pojo.Captcha
import com.kairlec.exception.ServiceErrorEnum
import com.kairlec.`interface`.ResponseDataInterface
import com.kairlec.config.editable.EditableConfig
import com.kairlec.dao.ConfigDao
import com.kairlec.local.utils.ResponseDataUtils
import com.kairlec.local.utils.UserUtils
import com.kairlec.pojo.StartupConfig
import com.kairlec.pojo.User
import org.apache.logging.log4j.LogManager
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*
import javax.servlet.http.Cookie
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse
import javax.servlet.http.HttpSession

/**
 *@program: SKExplorer
 *@description: 管理员接口
 *@author: Kairlec
 *@create: 2020-03-08 18:07
 */

@JsonRequestMapping(value = ["/admin"])
@RestController
class AdminController {
    @Autowired
    private lateinit var startupConfig: StartupConfig

    @RequestMapping(value = ["/captcha"])
    fun captcha(session: HttpSession, response: HttpServletResponse): ResponseDataInterface {
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
            session.setAttribute("captcha", Captcha.getInstant(startupConfig.captchaCount))
        }
        response.status = 403
        ServiceErrorEnum.UNKNOWN_REQUEST.throwout()
    }


    @RequestMapping(value = ["/login"])
    fun login(@CookieValue(value = "status", defaultValue = "") status: String, session: HttpSession, request: HttpServletRequest, response: HttpServletResponse): ResponseDataInterface {
        var user: User? = null
        if (status.isNotEmpty()) {
            user = UserUtils.authCookie(status, request)
        }
        if (user == null) {
            user = UserUtils.authLogin(request)
            logger.info("用户验证成功")
        }
        session.setAttribute("user", user)
        session.maxInactiveInterval = 60 * 60
        val cookie = Cookie("status", UserUtils.createCookie(user, request, 1000 * 60 * 60 * 24 * 7))
        cookie.path = "/"
        cookie.maxAge = 7 * 24 * 60 * 60
        cookie.isHttpOnly = true
        response.addCookie(cookie)
        return ResponseDataUtils.ok(user.username)
    }

    @RequestMapping(value = ["/login/key"])
    fun key(): ResponseDataInterface {
        return ResponseDataUtils.ok(ConfigDao.Instance.publicKey)
    }

    @RequestMapping(value = ["/logout"])
    fun logout(session: HttpSession): ResponseDataInterface {
        session.invalidate()
        return ResponseDataUtils.ok()
    }

    @RequestMapping(value = ["/status"], method = [RequestMethod.POST])
    fun status(@CookieValue(value = "status", defaultValue = "") status: String, session: HttpSession, request: HttpServletRequest, response: HttpServletResponse): ResponseDataInterface {
        var user = session.getAttribute("user") as? User
        if (user == null) {
            if (status.isNotEmpty()) {
                user = UserUtils.authCookie(status, request)
                logger.info(user)
            }
        } else {
            val checkStatus = UserUtils.authHttpSession(session)
            if (!checkStatus.OK()) {
                checkStatus.throwout()
            }
        }
        if (user == null) {
            ServiceErrorEnum.NOT_LOGGED_IN.throwout()
        }
        session.maxInactiveInterval = 60 * 60
        val cookie = Cookie("status", UserUtils.createCookie(user, request, 1000 * 60 * 60 * 24 * 7))
        cookie.path = "/"
        cookie.maxAge = 7 * 24 * 60 * 60
        cookie.isHttpOnly = true
        response.addCookie(cookie)
        return ResponseDataUtils.ok(user.username)
    }

    @RequestMapping(value = ["/config/{action}/{type}"])
    fun config(request: HttpServletRequest, @PathVariable action: String, @PathVariable type: String): ResponseDataInterface {
        when (action) {
            "get" -> {
                return when (type) {
                    "all" -> ResponseDataUtils.ok(EditableConfig.config.systemConfig)
                    "redirect" -> ResponseDataUtils.ok(EditableConfig.config.systemConfig.redirectEnable)
                    else -> {
                        ServiceErrorEnum.UNKNOWN_REQUEST.data(type).throwout()
                    }
                }
            }
            "update" -> {
                return when (type) {
                    "redirect" -> ResponseDataUtils.ok(EditableConfig.config.systemConfig.redirectEnable)
                    else -> {
                        ServiceErrorEnum.UNKNOWN_REQUEST.data(type).throwout()
                    }
                }
            }
            else -> {
                ServiceErrorEnum.UNKNOWN_REQUEST.data(action).throwout()
            }
        }
    }




    companion object {
        private val logger = LogManager.getLogger(AdminController::class.java)
    }
}
