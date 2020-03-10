package com.kairlec.utils.file


import java.net.URLDecoder
import java.nio.charset.StandardCharsets
import javax.servlet.http.HttpServletRequest

object FileUtils {
    fun getPathByRequest(request: HttpServletRequest, URIRoot: String): String {
        val requestURI = URLDecoder.decode(request.requestURI, StandardCharsets.UTF_8)
        return if (requestURI.endsWith("/$URIRoot/") || requestURI.endsWith("/$URIRoot")) {
            ""
        } else requestURI.substring(2 + URIRoot.length)
    }
}
