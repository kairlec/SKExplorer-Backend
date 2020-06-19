package com.kairlec.service

import com.kairlec.intf.ResponseDataInterface
import com.kairlec.model.bo.SystemConfig
import com.kairlec.model.bo.User
import com.kairlec.model.vo.Announcement
import com.kairlec.model.vo.Captcha
import org.springframework.stereotype.Service
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse
import javax.servlet.http.HttpSession

@Service
interface AdminService {
    fun captcha(captcha: Captcha): ResponseDataInterface
    fun freshCaptcha(captcha: Captcha, session: HttpSession)
    fun login(captcha: Captcha?, username: String, password: String, captchaRequestString: String?, session: HttpSession, request: HttpServletRequest, response: HttpServletResponse): ResponseDataInterface
    fun key(): ResponseDataInterface
    fun status(status: String?, user: User?, session: HttpSession, request: HttpServletRequest, response: HttpServletResponse): ResponseDataInterface
    fun logout(session: HttpSession, response: HttpServletResponse): ResponseDataInterface
    fun getSystemConfig(): ResponseDataInterface
    fun updateSystemConfig(systemConfig: SystemConfig): ResponseDataInterface
    fun updateAnnouncement(announcement: Announcement): ResponseDataInterface
    fun addAnnouncement(content: String): ResponseDataInterface
    fun removeAnnouncement(id: String): ResponseDataInterface
}