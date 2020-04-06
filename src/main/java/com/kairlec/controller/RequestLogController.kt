package com.kairlec.controller

import com.kairlec.annotation.JsonRequestMapping
import com.kairlec.intf.ResponseDataInterface
import com.kairlec.local.utils.FileUtils
import com.kairlec.local.utils.MultipartFileSender
import com.kairlec.local.utils.ResponseDataUtils.responseOK
import com.kairlec.model.vo.RelativePath
import com.kairlec.utils.content
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RequestParam
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
@JsonRequestMapping(value = ["/request"],method = [RequestMethod.POST])
@RestController
class RequestLogController {
    @RequestMapping(value = ["/content"])
    fun get() = "[" + File("Log/request.log").content + "]".responseOK

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
    fun file(@RequestParam(name = "sourceFile") sourceFile: RelativePath, request: HttpServletRequest, response: HttpServletResponse) = MultipartFileSender.fromPath(FileUtils.getLogPath("Log/Request", sourceFile.path), request, response).serveResource()
}