package com.kairlec.filter

import com.kairlec.local.utils.UserUtils
import org.springframework.stereotype.Component
import org.springframework.web.servlet.HandlerInterceptor
import java.util.*
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

/**
 *@program: SKExplorer
 *@description: 文件API拦截器
 *@author: Kairlec
 *@create: 2020-03-08 18:12
 */

@Component
class FileInterceptor : HandlerInterceptor {
    override fun preHandle(request: HttpServletRequest, response: HttpServletResponse, handler: Any?): Boolean {
        val checkStatus = UserUtils.authHttpServletRequest(request, blackAPIList)
        return if (checkStatus.ok) {
            true
        } else {
            response.contentType = "application/json;charset=UTF-8"
            response.writer.write(checkStatus.json)
            false
        }
    }

    companion object {
        private val blackAPIList: Array<String> = arrayOf("/file/download","/file/content","/file/list")
        var PathPatterns: List<String> = ArrayList(listOf(
                "/file",
                "/file/**"
        ))
    }
}

