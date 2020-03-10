package com.kairlec.contrller.admin

import com.kairlec.local.utils.ResponseDataUtils
import com.kairlec.local.utils.UserUtils
import org.apache.logging.log4j.LogManager
import org.springframework.stereotype.Component
import org.springframework.web.servlet.HandlerInterceptor
import java.util.*
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

/**
 *@program: SKExplorer
 *@description: 管理员拦截器
 *@author: Kairlec
 *@create: 2020-03-08 18:09
 */
@Component
class AdminInterceptor : HandlerInterceptor {
    override fun preHandle(request: HttpServletRequest, response: HttpServletResponse, handler: Any?): Boolean {
        val checkStatus = UserUtils.authHttpServletRequest(request, blackAPIList)
        return if (checkStatus.OK()) {
            true
        } else {
            response.writer.write(ResponseDataUtils.error(checkStatus))
            false
        }
    }

    companion object {
        private val logger = LogManager.getLogger(AdminController::class.java)
        private val blackAPIList: Array<String> = arrayOf("/admin/logout", "/admin/login", "/admin/login/key", "/admin/captcha", "/admin/captcha/fresh")
        var PathPatterns: List<String> = ArrayList(listOf(
                "/admin",
                "/admin/**"
        ))
    }
}