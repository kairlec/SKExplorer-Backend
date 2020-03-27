package com.kairlec.utils

import com.kairlec.constant.ServiceErrorEnum
import javax.servlet.http.HttpServletRequest

/**
 *@program: SKExplorer
 *@description: HttpServletRequest增强
 *@author: Kairlec
 *@create: 2020-03-12 09:16
 */

operator fun HttpServletRequest.get(parameter: String): String? {
    return this.getParameter(parameter)
}

fun HttpServletRequest.getSourcePath(): String {
    return this["source"] ?: ServiceErrorEnum.MISSING_REQUIRED_PARAMETERS.throwout()
}

fun HttpServletRequest.getTargetPath(): String {
    return this["target"] ?: ServiceErrorEnum.MISSING_REQUIRED_PARAMETERS.throwout()
}

