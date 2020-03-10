package com.kairlec.contrller.log

import com.kairlec.exception.ServiceErrorEnum
import com.kairlec.local.utils.MultipartFileSender
import com.kairlec.local.utils.RequestUtils
import com.kairlec.local.utils.ResponseDataUtils
import com.kairlec.local.utils.SKFileUtils
import com.kairlec.utils.file.GetFileContent
import org.apache.logging.log4j.Level
import org.apache.logging.log4j.LogManager
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.io.File
import java.util.*
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

/**
 *@program: SKExplorer
 *@description: 前端错误信息提交页面
 *@author: Kairlec
 *@create: 2020-03-08 18:12
 */

@RequestMapping("/ferror")
@RestController
class ErrorLogController {
    @RequestMapping(value = ["/content"])
    fun get(): String {
        return ResponseDataUtils.ok("[" + GetFileContent.byPathString("Log/frontend.log") + "]")
    }

    @RequestMapping(value = ["/list"])
    fun list(): String {
        val fileList: MutableList<String> = ArrayList()
        val file = File("Log/FrontEnd")
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
        MultipartFileSender.fromPath(SKFileUtils.getLogPath("Log/FrontEnd", sourcePath), request, response).serveResource()
    }

    @RequestMapping(value = ["/submit"])
    fun post(request: HttpServletRequest): String {
        val json = request.getParameter("object") ?: return ResponseDataUtils.error(ServiceErrorEnum.UNKNOWN_REQUEST)
        logger.log(Level.getLevel("FRONTEND"), json)
        return ResponseDataUtils.ok(Date().time)
    }

    companion object {
        private val logger = LogManager.getLogger(ErrorLogController::class.java)
    }
}

