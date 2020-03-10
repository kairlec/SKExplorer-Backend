package com.kairlec.utils

import javax.servlet.http.HttpServletRequest

object Network {
    fun getIpAddress(request: HttpServletRequest): String { // 获取请求主机IP地址,如果通过代理进来，则透过防火墙获取真实IP地址
        var ip = request.getHeader("X-Forwarded-For")
        if (ip == null || ip.isEmpty() || "unknown".equals(ip, ignoreCase = true)) {
            if (ip == null || ip.isEmpty() || "unknown".equals(ip, ignoreCase = true)) {
                ip = request.getHeader("Proxy-Client-IP")
            }
            if (ip == null || ip.isEmpty() || "unknown".equals(ip, ignoreCase = true)) {
                ip = request.getHeader("WL-Proxy-Client-IP")
            }
            if (ip == null || ip.isEmpty() || "unknown".equals(ip, ignoreCase = true)) {
                ip = request.getHeader("HTTP_CLIENT_IP")
            }
            if (ip == null || ip.isEmpty() || "unknown".equals(ip, ignoreCase = true)) {
                ip = request.getHeader("HTTP_X_FORWARDED_FOR")
            }
            if (ip == null || ip.isEmpty() || "unknown".equals(ip, ignoreCase = true)) {
                ip = request.remoteAddr
            }
        } else if (ip.length > 15) {
            ip.split(",").toTypedArray().forEach {
                if (!"unknown".equals(it, ignoreCase = true)) {
                    return it
                }
            }
        }
        return ip
    }
}