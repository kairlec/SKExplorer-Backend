package com.kairlec.contrller.log

import com.kairlec.exception.ServiceErrorEnum
import com.kairlec.local.utils.MultipartFileSender
import com.kairlec.local.utils.RequestUtils
import com.kairlec.local.utils.ResponseDataUtils
import com.kairlec.local.utils.SKFileUtils
import com.kairlec.utils.file.GetFileContent
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
@RequestMapping("/request")
@RestController
class RequestLogController {
    @RequestMapping(value = ["/content"])
    fun get(): String {
        return ResponseDataUtils.ok("[" + GetFileContent.byPathString("Log/request.log") + "]")
    }

    @RequestMapping(value = ["/list"])
    fun list(): String {
        val fileList: MutableList<String> = ArrayList()
        val file = File("Log/Request")
        if (file.exists()) {
            val files = file.listFiles()
            if (files != null) {
                for (subFile in files) {
                    fileList.add(subFile.name)
                }
            }
        }
        return ResponseDataUtils.ok(fileList)
    }

    @RequestMapping(value = ["/download"])
    fun file(request: HttpServletRequest, response: HttpServletResponse) {
        val sourcePath = RequestUtils.getSourcePath(request) ?: ServiceErrorEnum.MISSING_REQUIRED_PARAMETERS.throwout()
        MultipartFileSender.fromPath(SKFileUtils.getLogPath("Log/Request", sourcePath), request, response).serveResource()
    }
}