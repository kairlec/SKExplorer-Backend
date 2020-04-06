package com.kairlec.service.impl

import com.kairlec.constant.ServiceErrorEnum
import com.kairlec.controller.AdminController
import com.kairlec.dao.ConfigDao
import com.kairlec.intf.ResponseDataInterface
import com.kairlec.local.utils.ResponseDataUtils.responseOK
import com.kairlec.local.utils.UserUtils
import com.kairlec.model.bo.StartupConfig
import com.kairlec.model.bo.SystemConfig
import com.kairlec.model.bo.User
import com.kairlec.model.vo.Announcement
import com.kairlec.model.vo.Captcha
import com.kairlec.service.AdminService
import com.kairlec.utils.IP
import org.apache.logging.log4j.LogManager
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import javax.servlet.http.Cookie
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse
import javax.servlet.http.HttpSession

@Service
class AdminServiceImpl : AdminService {
    @Autowired
    private lateinit var startupConfig: StartupConfig

    @Autowired
    private lateinit var configServiceImpl: ConfigServiceImpl

    @Autowired
    private lateinit var announcementServiceImpl: AnnouncementServiceImpl

    override fun captcha(captcha: Captcha): ResponseDataInterface {
        return captcha.skImage.toBase64().responseOK
    }

    override fun freshCaptcha(captcha: Captcha, session: HttpSession) {
        session.setAttribute("captcha", Captcha.getInstant(startupConfig.captchaCount))
    }

    override fun login(captcha: Captcha?,
                       username: String,
                       password: String,
                       captchaRequestString: String?,
                       session: HttpSession,
                       request: HttpServletRequest,
                       response: HttpServletResponse): ResponseDataInterface {
        val user = UserUtils.authLogin(request.IP, session, captchaRequestString, captcha, username, password)
        logger.info("用户验证成功")
        session.setAttribute("user", user)
        session.maxInactiveInterval = 60 * 60
        val cookie = Cookie("status", UserUtils.createCookie(user, request, 1000 * 60 * 60 * 24 * 7))
        cookie.path = "/"
        cookie.maxAge = 7 * 24 * 60 * 60
        cookie.isHttpOnly = true
        response.addCookie(cookie)
        return user.username.responseOK
    }

    override fun key(): ResponseDataInterface {
        return ConfigDao.Instance.publicKey.responseOK
    }

    override fun logout(session: HttpSession, response: HttpServletResponse): ResponseDataInterface {
        session.invalidate()
        val cookie = Cookie("status", null)
        cookie.maxAge = 0 //立即使cookie失效
        cookie.path = "/"
        cookie.isHttpOnly = true
        response.addCookie(cookie)
        return null.responseOK
    }

    override fun status(status: String?,
                        user: User?,
                        session: HttpSession,
                        request: HttpServletRequest,
                        response: HttpServletResponse): ResponseDataInterface {
        var authUser: User? = user
        if (user == null) {
            status?.let {
                if (it.isNotEmpty()) {
                    authUser = UserUtils.authCookie(it, request)
                }
            }
        } else {
            val checkStatus = UserUtils.authHttpSession(session)
            if (checkStatus.bad) {
                checkStatus.throwout()
            }
        }
        if (authUser == null) {
            ServiceErrorEnum.NOT_LOGGED_IN.throwout()
        }
        session.maxInactiveInterval = 60 * 60
        val cookie = Cookie("status", UserUtils.createCookie(authUser!!, request, 1000 * 60 * 60 * 24 * 7))
        cookie.path = "/"
        cookie.maxAge = 7 * 24 * 60 * 60
        cookie.isHttpOnly = true
        response.addCookie(cookie)
        return authUser!!.username.responseOK
    }

    override fun getSystemConfig(): ResponseDataInterface {
        return configServiceImpl.getSystemConfig().responseOK
    }


    override fun updateSystemConfig(systemConfig: SystemConfig): ResponseDataInterface {
        configServiceImpl.setSystemConfig(systemConfig)
        return systemConfig.responseOK
    }

    override fun updateAnnouncement(announcement: Announcement): ResponseDataInterface {
        announcementServiceImpl.update(announcement)
        return announcement.responseOK
    }

    override fun addAnnouncement(content: String): ResponseDataInterface {
        return announcementServiceImpl.add(content).responseOK
    }

    override fun removeAnnouncement(id: String): ResponseDataInterface {
        announcementServiceImpl.delete(id)
        return null.responseOK
    }

    companion object {
        private val logger = LogManager.getLogger(AdminController::class.java)
    }
}