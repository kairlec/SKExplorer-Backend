package com.kairlec.controller

import com.kairlec.intf.ResponseDataInterface
import com.kairlec.local.utils.ResponseDataUtils.responseError
import org.apache.logging.log4j.LogManager
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseBody
import javax.servlet.http.HttpServletResponse

/**
 *@program: SKExplorer
 *@description: 异常处理器
 *@author: Kairlec
 *@create: 2020-03-08 18:10
 */
@ControllerAdvice
class SKExceptionHandler {
    @ExceptionHandler
    @ResponseBody
    fun exception(e: Exception, response: HttpServletResponse): ResponseDataInterface {
        val serviceError = e.responseError
        if (serviceError.code == 90003) {
            response.status = 500
        }
        return serviceError
    }

    companion object {
        private val logger = LogManager.getLogger(SKExceptionHandler::class.java)
    }
}
