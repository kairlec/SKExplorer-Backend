package com.kairlec.contrller.error

import com.kairlec.exception.ServiceErrorEnum
import com.kairlec.local.utils.ResponseDataUtils
import org.springframework.boot.web.servlet.error.ErrorController
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseBody
import javax.servlet.http.HttpServletRequest

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

    @RequestMapping(value = ["/error"])
    @ResponseBody
    fun error(request: HttpServletRequest): String {
        return ResponseDataUtils.error(ServiceErrorEnum.UNKNOWN_REQUEST.data(request.requestURI))
    }
}