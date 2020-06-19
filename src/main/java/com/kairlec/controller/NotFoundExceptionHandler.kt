package com.kairlec.controller

import com.kairlec.constant.ServiceErrorEnum
import com.kairlec.intf.ResponseDataInterface
import com.kairlec.annotation.JsonRequestMapping
import org.springframework.boot.web.servlet.error.ErrorController
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.ResponseBody

/**
 *@program: SKExplorer
 *@description: 无效URI异常拦截
 *@author: Kairlec
 *@create: 2020-03-08 18:10
 */
@Controller
class NotFoundExceptionHandler : ErrorController {
    override fun getErrorPath(): String {
        return "/error"
    }

    @JsonRequestMapping(value = ["/error"])
    @ResponseBody
    fun error(): ResponseDataInterface {
        return ServiceErrorEnum.UNKNOWN_REQUEST
    }
}