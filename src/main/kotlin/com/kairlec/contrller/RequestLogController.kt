package com.kairlec.contrller

import com.kairlec.annotation.JsonRequestMapping
import com.kairlec.`interface`.ResponseDataInterface
import com.kairlec.local.utils.MultipartFileSender
import com.kairlec.local.utils.ResponseDataUtils.responseOK
import com.kairlec.local.utils.SKFileUtils
import com.kairlec.utils.content
import com.kairlec.utils.getSourcePath
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.io.File
import java.util.*
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

/**
 *@program: SKExplorer
 *@description: 请求日志记录器API
 *@author: Kairlec
 *@create: 2020-03-08 18:13
 */
@JsonRequestMapping(value = ["/request"])
@RestController
class RequestLogController {
    @RequestMapping(value = ["/content"])
    fun get() = "[" + File("Log/request.log").content() + "]".responseOK

    @RequestMapping(value = ["/list"])
    fun list(): ResponseDataInterface {
        val fileList: MutableList<String> = ArrayList()
        val file = File("Log/Request")
        if (file.exists()) {
            file.listFiles()?.let { files ->
                files.forEach { fileList.add(it.name) }
            }
        }
        return fileList.responseOK
    }

    @RequestMapping(value = ["/download"])
    fun file(request: HttpServletRequest, response: HttpServletResponse) = MultipartFileSender.fromPath(SKFileUtils.getLogPath("Log/Request", request.getSourcePath()), request, response).serveResource()
}