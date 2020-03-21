package com.kairlec.pojo.json

import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonProperty
import com.kairlec.utils.LocalConfig.Companion.toJSON
import com.kairlec.utils.IP
import java.net.URLDecoder
import java.nio.charset.StandardCharsets
import java.util.*
import javax.servlet.http.Cookie
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

class HTTPInfo(request: HttpServletRequest, response: HttpServletResponse) {
    @JsonProperty("Method")
    val method: String? = request.method

    @JsonProperty("URI")
    val uri: String? = URLDecoder.decode(request.requestURI, StandardCharsets.UTF_8)

    @JsonProperty("RemoteIP")
    val remoteIP: String? = request.IP

    @JsonProperty("QueryString")
    val queryString: String? = request.queryString

    @JsonProperty("URL")
    val url: String? = URLDecoder.decode(request.requestURL.toString(), StandardCharsets.UTF_8)

    @JsonProperty("Scheme")
    val scheme: String? = request.scheme

    @JsonProperty("Protocol")
    val proto: String? = request.protocol

    @JsonProperty("RemoteUser")
    val remoteUser: String? = request.remoteUser

    @JsonProperty("WebName")
    val webName: String? = request.contextPath

    @JsonProperty("Headers")
    val headers: MutableMap<String, String?>?

    @JsonProperty("Parameters")
    val parameters: MutableMap<String, String?>?

    @JsonProperty("Cookies")
    val cookies: Array<Cookie>? = request.cookies

    @JsonProperty("ResponseStatus")
    val responseStatus = response.status

    val json
        @JsonIgnore
        get() = String.toJSON(this)


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