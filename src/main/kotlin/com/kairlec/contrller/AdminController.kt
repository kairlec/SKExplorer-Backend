package com.kairlec.contrller

import com.kairlec.annotation.JsonRequestMapping
import com.kairlec.model.vo.Captcha
import com.kairlec.constant.ServiceErrorEnum
import com.kairlec.`interface`.ResponseDataInterface
import com.kairlec.config.editable.EditableConfig
import com.kairlec.dao.ConfigDao
import com.kairlec.local.utils.ResponseDataUtils.responseOK
import com.kairlec.local.utils.UserUtils
import com.kairlec.model.bo.StartupConfig
import com.kairlec.model.bo.User
import com.kairlec.utils.get
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
            return captcha.skImage.toBase64().responseOK
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
        return user.username.responseOK
    }

    @RequestMapping(value = ["/login/key"])
    fun key(): ResponseDataInterface {
        return ConfigDao.Instance.publicKey.responseOK
    }

    @RequestMapping(value = ["/logout"])
    fun logout(session: HttpSession): ResponseDataInterface {
        session.invalidate()
        return null.responseOK
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
            if (checkStatus.bad) {
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
        return user.username.responseOK
    }

    @RequestMapping(value = ["/config/{action}/{type}"])
    fun config(request: HttpServletRequest, @PathVariable action: String, @PathVariable type: String): ResponseDataInterface {
        when (action) {
            "get" -> {
                return when (type) {
                    "all" -> EditableConfig.config.systemConfig.responseOK
                    "redirect" -> EditableConfig.config.systemConfig.redirectEnable.responseOK
                    else -> {
                        ServiceErrorEnum.UNKNOWN_REQUEST.data(type).throwout()
                    }
                }
            }
            "update" -> {
                return when (type) {
                    "redirect" -> updateRedirect(request)
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


    private fun updateRedirect(request: HttpServletRequest): ResponseDataInterface {
        val enable = request["redirect"]?.toBoolean() ?: ServiceErrorEnum.MISSING_REQUIRED_PARAMETERS.throwout()
        EditableConfig.config.systemConfig.redirectEnable = enable
        EditableConfig.save()
        return enable.responseOK
    }


    companion object {
        private val logger = LogManager.getLogger(AdminController::class.java)
    }
}
