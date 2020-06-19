package com.kairlec.utils

import com.kairlec.constant.ServiceErrorEnum
import com.kairlec.model.vo.RelativePath
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

fun HttpServletRequest.getSourcePath(): RelativePath {
    return this["source"]?.let { RelativePath(it) } ?: ServiceErrorEnum.MISSING_REQUIRED_PARAMETERS.throwout()
}

fun HttpServletRequest.getTargetPath(): RelativePath {
    return this["target"]?.let { RelativePath(it) } ?: ServiceErrorEnum.MISSING_REQUIRED_PARAMETERS.throwout()
}

