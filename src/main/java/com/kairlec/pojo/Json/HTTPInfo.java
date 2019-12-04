package com.kairlec.pojo.Json;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.kairlec.utils.Network;
import lombok.Data;


import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.*;

@Data
public class HTTPInfo {
    @JSONField(name = "Scheme", ordinal = 7)
    private String Scheme;
    @JSONField(name = "Proto", ordinal = 8)
    private String Proto;
    @JSONField(name = "ResponseStatus", ordinal = 12)
    private Integer ResponseStatus;
    @JSONField(name = "URL", ordinal = 0)
    private String URL;
    @JSONField(name = "URI", ordinal = 1)
    private String URI;
    @JSONField(name = "QueryString", ordinal = 2)
    private String QueryString;
    @JSONField(name = "RemoteIP", ordinal = 3)
    private String RemoteIP;
    @JSONField(name = "RemoteUser", ordinal = 4)
    private String RemoteUser;
    @JSONField(name = "Method", ordinal = 5)
    private String Method;
    @JSONField(name = "WebName", ordinal = 6)
    private String WebName;
    @JSONField(name = "Headers", ordinal = 9)
    private Map<String, String> Headers;
    @JSONField(name = "Parameters", ordinal = 11)
    private Map<String, String> Parameters;
    @JSONField(name = "Cookies", ordinal = 10)
    private Cookie[] Cookies;

    public HTTPInfo(HttpServletRequest request, HttpServletResponse response) {
        URL = URLDecoder.decode(request.getRequestURL().toString(), StandardCharsets.UTF_8);
        URI = URLDecoder.decode(request.getRequestURI(), StandardCharsets.UTF_8);
        QueryString = request.getQueryString();
        RemoteIP = Network.getIpAddress(request);
        Method = request.getMethod();
        WebName = request.getContextPath();
        RemoteUser = request.getRemoteUser();
        Headers = new HashMap<>();
        Enumeration<String> headerNames = request.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            String name = headerNames.nextElement();
            String value = request.getHeader(name);
            Headers.put(name, value);
        }
        Parameters = new HashMap<>();
        Enumeration<String> parameterNames = request.getParameterNames();
        while (parameterNames.hasMoreElements()) {
            String name = parameterNames.nextElement();
            String value = request.getHeader(name);
            Parameters.put(name, value);
        }
        ResponseStatus = response.getStatus();
        Proto = request.getProtocol();
        Scheme = request.getScheme();
        Cookies = request.getCookies();
    }

    @Override
    public String toString() {
        return JSON.toJSONString(this, SerializerFeature.WriteMapNullValue);
    }
}
