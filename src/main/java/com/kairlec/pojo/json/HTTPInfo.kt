package com.kairlec.pojo.json

import com.kairlec.utils.Network
import com.alibaba.fastjson.JSON
import com.alibaba.fastjson.annotation.JSONField
import com.alibaba.fastjson.serializer.SerializerFeature
import java.net.URLDecoder
import java.nio.charset.StandardCharsets
import java.util.*
import javax.servlet.http.Cookie
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

class HTTPInfo(request: HttpServletRequest, response: HttpServletResponse) {
    @JSONField(name = "Scheme", ordinal = 7)
    val scheme: String? = request.scheme
    @JSONField(name = "Protocol", ordinal = 8)
    val proto: String? = request.protocol
    @JSONField(name = "ResponseStatus", ordinal = 12)
    val responseStatus = response.status
    @JSONField(name = "URL", ordinal = 0)
    val url: String? = URLDecoder.decode(request.requestURL.toString(), StandardCharsets.UTF_8)
    @JSONField(name = "URI", ordinal = 1)
    val uri: String? = URLDecoder.decode(request.requestURI, StandardCharsets.UTF_8)
    @JSONField(name = "QueryString", ordinal = 2)
    val queryString: String? = request.queryString
    @JSONField(name = "RemoteIP", ordinal = 3)
    val remoteIP: String? = Network.getIpAddress(request)
    @JSONField(name = "RemoteUser", ordinal = 4)
    val remoteUser: String? = request.remoteUser
    @JSONField(name = "Method", ordinal = 5)
    val method: String? = request.method
    @JSONField(name = "WebName", ordinal = 6)
    val webName: String? = request.contextPath
    @JSONField(name = "Headers", ordinal = 9)
    val headers: MutableMap<String, String?>?
    @JSONField(name = "Parameters", ordinal = 11)
    val parameters: MutableMap<String, String?>?
    @JSONField(name = "Cookies", ordinal = 10)
    val cookies: Array<Cookie>? = request.cookies

    override fun toString(): String {
        return JSON.toJSONString(this, SerializerFeature.WriteMapNullValue)
    }

    init {
        headers = HashMap()
        val headerNames = request.headerNames
        while (headerNames.hasMoreElements()) {
            val name = headerNames.nextElement()
            val value = request.getHeader(name)
            headers[name] = value
        }
        parameters = HashMap()
        val parameterNames = request.parameterNames
        while (parameterNames.hasMoreElements()) {
            val name = parameterNames.nextElement()
            val value = request.getParameter(name)
            parameters[name] = value
        }
    }
}