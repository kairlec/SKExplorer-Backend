package com.kairlec.local.utils;

import com.kairlec.exception.ServiceError;
import com.kairlec.pojo.ResponseData;

import java.io.ByteArrayOutputStream;

public abstract class ResponseDataUtils {
    private ResponseDataUtils() {
        throw new AssertionError("工具类不允许实例化");
    }

    public static ServiceError fromException(Exception e) {
        ByteArrayOutputStream buf = new java.io.ByteArrayOutputStream();
        e.printStackTrace(new java.io.PrintWriter(buf, true));
        String expMessage = buf.toString();
        try {
            buf.close();
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return ServiceError.Error(90003, expMessage);
    }

    public static String OK(){
        return ServiceError.NO_ERROR.toString();
    }

    public static String Error(Exception e) {
        return fromException(e).toString();
    }

    public static String Error(int code, String message) {
        return ServiceError.Error(code, message).toString();
    }

    public static String Error(ServiceError serviceError){
        return serviceError.toString();
    }

    public static String successData(Object object) {
        return new ResponseData(0, "OK", object).toString();
    }

}
