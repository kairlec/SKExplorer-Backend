package com.kairlec.local.utils;

import javax.servlet.http.HttpServletRequest;

public abstract class RequestUtils {
    private RequestUtils() {
        throw new AssertionError("工具类不允许实例化");
    }

    public static String getSourcePath(HttpServletRequest request) {
        return request.getParameter("source");
    }

    public static String getTargetPath(HttpServletRequest request) {
        return request.getParameter("target");
    }

}
