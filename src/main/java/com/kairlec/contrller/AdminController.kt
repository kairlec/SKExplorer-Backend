package com.kairlec.contrller

import com.kairlec.annotation.JsonRequestMapping
import com.kairlec.model.vo.Captcha
import com.kairlec.constant.ServiceErrorEnum
import com.kairlec.intf.ResponseDataInterface
import com.kairlec.dao.ConfigDao
import com.kairlec.local.utils.ResponseDataUtils.responseOK
import com.kairlec.local.utils.UserUtils
import com.kairlec.model.bo.StartupConfig
import com.kairlec.model.bo.SystemConfig
import com.kairlec.model.bo.User
import com.kairlec.model.vo.Announcement
import com.kairlec.service.impl.AnnouncementServiceImpl
import com.kairlec.service.impl.ConfigServiceImpl
import com.kairlec.utils.LocalConfig
import com.kairlec.utils.LocalConfig.Companion.toObject
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

    @Autowired
    private lateinit var configServiceImpl: ConfigServiceImpl

    @Autowired
    private lateinit var announcementServiceImpl: AnnouncementServiceImpl

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

    @RequestMapping(value = ["/config/system/get"])
    fun getSystemConfig(): ResponseDataInterface {
        return configServiceImpl.getSystemConfig().responseOK
    }


    @RequestMapping(value = ["/config/system/update"])
    fun updateSystemConfig(@RequestBody systemConfig: SystemConfig): ResponseDataInterface {
        configServiceImpl.setSystemConfig(systemConfig)
        return systemConfig.responseOK
    }

    @RequestMapping(value = ["/announcement/update"])
    fun updateAnnouncement(@RequestBody announcement: Announcement): ResponseDataInterface {
        announcementServiceImpl.update(announcement)
        return announcement.responseOK
    }

    @RequestMapping(value = ["/announcement/add"])
    fun addAnnouncement(request: HttpServletRequest): ResponseDataInterface {
        val content = request["content"] ?: ServiceErrorEnum.MISSING_REQUIRED_PARAMETERS.data("content").throwout()
        return announcementServiceImpl.add(content).responseOK
    }

    @RequestMapping(value = ["/announcement/remove"])
    fun removeAnnouncement(request: HttpServletRequest): ResponseDataInterface {
        val id = request["id"] ?: ServiceErrorEnum.MISSING_REQUIRED_PARAMETERS.data("id").throwout()
        announcementServiceImpl.delete(id)
        return null.responseOK
    }



    companion object {
        private val logger = LogManager.getLogger(AdminController::class.java)
    }
}
