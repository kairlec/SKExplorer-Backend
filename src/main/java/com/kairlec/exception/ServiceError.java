package com.kairlec.exception;

import com.alibaba.fastjson.annotation.JSONType;
import com.kairlec.pojo.ResponseData;

/**
 * 业务错误码
 */
@JSONType(serializeEnumAsJavaBean = true)
public class ServiceError extends ResponseData {
    //无异常
    public static ServiceError NO_ERROR = new ServiceError(0, "OK");

    //未指名的异常
    public static ServiceError UNSPECIFIED = new ServiceError(90001, "未指名的错误");
    public static ServiceError UNKNOWN = new ServiceError(90002, "未知错误");

    //请求异常
    public static ServiceError UNKNOWN_REQUEST = new ServiceError(400, "未知的请求");
    public static ServiceError FILE_NOT_EXISTS = new ServiceError(404, "文件不存在");
    public static ServiceError NO_CONTENT = new ServiceError(204, "无内容可显示");
    public static ServiceError INVALID_DIR = new ServiceError(10001, "无效的文件夹");
    public static ServiceError INVALID_FILE = new ServiceError(10002, "无效的文件");
    public static ServiceError NOT_DIR = new ServiceError(10003, "不是文件夹");
    public static ServiceError NOT_FILE = new ServiceError(10004, "不是文件");
    public static ServiceError NOT_MULTIPART_FROM_DATA = new ServiceError(10005, "不是multipart/form-data请求");
    public static ServiceError MISSING_REQUIRED_PARAMETERS = new ServiceError(10006, "缺少必要的参数");

    //登录异常
    public static ServiceError USERNAME_NOT_EXISTS = new ServiceError(30001, "用户名不存在");
    public static ServiceError WRONG_PASSWORD = new ServiceError(30002, "密码错误");
    public static ServiceError EXPIRED_LOGIN = new ServiceError(30003, "登录状态已过期");
    public static ServiceError NULL_USERNAME = new ServiceError(30004, "用户名为空");
    public static ServiceError NULL_PASSWORD = new ServiceError(30005, "密码为空");
    public static ServiceError UNTRUSTED_IP = new ServiceError(30006, "不受信任的IP");
    public static ServiceError NOT_LOGGED_IN = new ServiceError(30007, "未登录");
    public static ServiceError HAD_LOGGED_IN = new ServiceError(30008, "已有登录用户");
    public static ServiceError NEED_VERIFY = new ServiceError(30009, "请求的权限需要验证");
    public static ServiceError WRONG_CAPTCHA = new ServiceError(30010, "验证码错误");
    public static ServiceError NULL_CAPTCHA = new ServiceError(30011, "验证码为空");
    public static ServiceError UNKNOWN_PASSWORD = new ServiceError(30012, "未知的密码串");

    //服务器异常
    public static ServiceError IO_EXCEPTION = new ServiceError(50001, "IO出现错误");
    public static ServiceError INITIALIZE_FAILED = new ServiceError(50003, "初始化系统出现错误");


    private ServiceError(int code, String message, Object data) {
        super(code, message, data);
    }

    private ServiceError(int code, String message) {
        super(code, message, null);
    }

    public static ServiceError Error(int code, String message) {
        return new ServiceError(code, message);
    }

    public static ServiceError Error(int code, String message, Object data) {
        return new ServiceError(code, message, data);
    }

    public boolean OK() {
        return this.getCode() == 0;
    }

}