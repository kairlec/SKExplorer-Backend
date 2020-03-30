package com.kairlec.contrller

import com.kairlec.annotation.JsonRequestMapping
import com.kairlec.constant.ServiceErrorEnum
import com.kairlec.intf.ResponseDataInterface
import com.kairlec.local.utils.FileUtils
import com.kairlec.local.utils.MultipartFileSender
import com.kairlec.local.utils.ResponseDataUtils.responseOK
import com.kairlec.utils.content
import com.kairlec.utils.get
import com.kairlec.utils.getSourcePath
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

@JsonRequestMapping(value = ["/ferror"])
@RestController
class ErrorLogController {
    @RequestMapping(value = ["/content"])
    fun get() = "[" + File("Log/frontend.log").content + "]".responseOK


    @RequestMapping(value = ["/list"])
    fun list(): ResponseDataInterface {
        val fileList: MutableList<String> = ArrayList()
        val file = File("Log/FrontEnd")
        if (file.exists()) {
            file.listFiles()?.let { files ->
                files.forEach { fileList.add(it.name) }
            }
        }
        return fileList.responseOK
    }

    @RequestMapping(value = ["/download"])
    fun file(request: HttpServletRequest, response: HttpServletResponse) = MultipartFileSender.fromPath(FileUtils.getLogPath("Log/FrontEnd", request.getSourcePath().path), request, response).serveResource()

    @RequestMapping(value = ["/submit"])
    fun post(request: HttpServletRequest): ResponseDataInterface {
        val json = request["object"] ?: ServiceErrorEnum.UNKNOWN_REQUEST.throwout()
        logger.log(Level.getLevel("FRONTEND"), json)
        return null.responseOK
    }

    companion object {
        private val logger = LogManager.getLogger(ErrorLogController::class.java)
    }
}

