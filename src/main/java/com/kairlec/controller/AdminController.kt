package com.kairlec.controller

import com.kairlec.annotation.JsonRequestMapping
import com.kairlec.constant.ServiceErrorEnum
import com.kairlec.intf.ResponseDataInterface
import com.kairlec.model.bo.SystemConfig
import com.kairlec.model.bo.User
import com.kairlec.model.vo.Announcement
import com.kairlec.model.vo.Captcha
import com.kairlec.service.impl.AdminServiceImpl
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse
import javax.servlet.http.HttpSession

/**
 *@program: SKExplorer
 *@description: 管理员接口
 *@author: Kairlec
 *@create: 2020-03-08 18:07
 */

@JsonRequestMapping(value = ["/admin"],method = [RequestMethod.POST])
@RestController
class AdminController {
    @Autowired
    private lateinit var adminServiceImpl: AdminServiceImpl

    @RequestMapping(value = ["/captcha"])
    fun captcha(@SessionAttribute(name = "captcha") captcha: Captcha): ResponseDataInterface {
        return adminServiceImpl.captcha(captcha)
    }

    @RequestMapping(value = ["/captcha/fresh"])
    fun freshCaptcha(@SessionAttribute(name = "captcha") captcha: Captcha, session: HttpSession) {
        adminServiceImpl.freshCaptcha(captcha, session)
    }

    @RequestMapping(value = ["/login"])
    fun login(@SessionAttribute(name = "user", required = false) loggedUser: User?,
              @SessionAttribute(name = "captcha", required = false) captcha: Captcha?,
              @RequestParam(name = "username") username: String,
              @RequestParam(name = "password") password: String,
              @RequestParam(name = "captcha", required = false) captchaRequestString: String?,
              session: HttpSession,
              request: HttpServletRequest,
              response: HttpServletResponse
    ): ResponseDataInterface {
        if (loggedUser != null) {
            ServiceErrorEnum.HAD_LOGGED_IN.throwout()
        }
        return adminServiceImpl.login(captcha, username, password, captchaRequestString, session, request, response)
    }

    @RequestMapping(value = ["/login/key"])
    fun key(): ResponseDataInterface {
        return adminServiceImpl.key()
    }

    @RequestMapping(value = ["/logout"])
    fun logout(session: HttpSession, response: HttpServletResponse): ResponseDataInterface {
        return adminServiceImpl.logout(session, response)
    }

    @RequestMapping(value = ["/status"])
    fun status(@CookieValue(value = "status", required = false) status: String?,
               @SessionAttribute(name = "user", required = false) user: User?,
               session: HttpSession,
               request: HttpServletRequest,
               response: HttpServletResponse
    ): ResponseDataInterface {
        return adminServiceImpl.status(status, user, session, request, response)
    }

    @RequestMapping(value = ["/config/system/get"])
    fun getSystemConfig(): ResponseDataInterface {
        return adminServiceImpl.getSystemConfig()
    }


    @RequestMapping(value = ["/config/system/update"])
    fun updateSystemConfig(@RequestBody systemConfig: SystemConfig): ResponseDataInterface {
        return adminServiceImpl.updateSystemConfig(systemConfig)
    }

    @RequestMapping(value = ["/announcement/update"])
    fun updateAnnouncement(@RequestBody announcement: Announcement): ResponseDataInterface {
        return adminServiceImpl.updateAnnouncement(announcement)
    }

    @RequestMapping(value = ["/announcement/add"])
    fun addAnnouncement(@RequestParam(name = "content") content: String): ResponseDataInterface {
        return adminServiceImpl.addAnnouncement(content)
    }

    @RequestMapping(value = ["/announcement/remove"])
    fun removeAnnouncement(@RequestParam(name = "id") id: String): ResponseDataInterface {
        return adminServiceImpl.removeAnnouncement(id)
    }

}
