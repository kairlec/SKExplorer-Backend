package com.kairlec.local.utils

import javax.servlet.http.HttpServletRequest;

object RequestUtils {
    fun getSourcePath(request: HttpServletRequest): String? {
        return request.getParameter("source")
    }

    fun getTargetPath(request: HttpServletRequest): String? {
        return request.getParameter("target")
    }
}
