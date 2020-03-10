package com.kairlec.contrller.file

import com.kairlec.local.utils.ResponseDataUtils
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
        val checkStatus = UserUtils.authHttpServletRequest(request,blackAPIList)
        return if (checkStatus.OK()) {
            true
        } else {
            response.writer.write(ResponseDataUtils.error(checkStatus))
            false
        }
    }

    companion object {
        private val blackAPIList: Array<String> = arrayOf("/file/download","/file/download2","/file/get","/file/list")
        var PathPatterns: List<String> = ArrayList(listOf(
                "/file",
                "/file/**"
        ))
    }
}

