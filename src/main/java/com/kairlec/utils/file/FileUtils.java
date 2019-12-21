package com.kairlec.utils.file;


import javax.servlet.http.HttpServletRequest;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;

public abstract class FileUtils {
    private FileUtils() {
        throw new AssertionError("工具类不允许实例化");
    }

    public static String getPathByRequest(HttpServletRequest request, String URIRoot) {
        String requestURI = URLDecoder.decode(request.getRequestURI(), StandardCharsets.UTF_8);
        if (requestURI.endsWith("/" + URIRoot + "/") || requestURI.endsWith("/" + URIRoot)) {
            return "";
        }
        return requestURI.substring(2 + URIRoot.length());
    }
}
