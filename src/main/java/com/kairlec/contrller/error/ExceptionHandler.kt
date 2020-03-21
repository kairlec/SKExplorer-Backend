package com.kairlec.contrller.error

import com.kairlec.`interface`.ResponseDataInterface
import com.kairlec.local.utils.ResponseDataUtils
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
class ExceptionHandler {
    @ExceptionHandler
    @ResponseBody
    fun exception(e: Exception): ResponseDataInterface {
        return ResponseDataUtils.error(e)
    }
}
