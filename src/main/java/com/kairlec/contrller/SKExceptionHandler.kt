package com.kairlec.contrller

import com.kairlec.intf.ResponseDataInterface
import com.kairlec.local.utils.ResponseDataUtils.responseError
import org.apache.logging.log4j.LogManager
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseBody

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
    fun exception(e: Exception): ResponseDataInterface {
        logger.error(e.message,e)
        return e.responseError
    }

    companion object {
        private val logger = LogManager.getLogger(SKExceptionHandler::class.java)
    }
}
