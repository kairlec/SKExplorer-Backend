package com.kairlec.exception;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson.annotation.JSONType;
import com.alibaba.fastjson.serializer.SerializerFeature;

/**
 * 业务错误码
 */
@JSONType(serializeEnumAsJavaBean = true)
public class ErrorCodeClass implements ErrorCode {
    //无异常
    public static ErrorCodeClass NO_ERROR = new ErrorCodeClass(0, null);

    //未指名的异常
    public static ErrorCodeClass UNSPECIFIED = new ErrorCodeClass(90001, "未指名的错误");
    public static ErrorCodeClass UNKNOWN = new ErrorCodeClass(90002, "未知错误");

    //请求异常
    public static ErrorCodeClass UNKNOWN_REQUEST = new ErrorCodeClass(400, "未知的请求");
    public static ErrorCodeClass FILE_NOT_EXISTS = new ErrorCodeClass(404, "文件不存在");
    public static ErrorCodeClass NO_CONTENT = new ErrorCodeClass(204, "无内容可显示");
    public static ErrorCodeClass INVALID_DIR = new ErrorCodeClass(10001, "无效的文件夹");
    public static ErrorCodeClass INVALID_FILE = new ErrorCodeClass(10002, "无效的文件");
    public static ErrorCodeClass NOT_DIR = new ErrorCodeClass(10003, "不是文件夹");
    public static ErrorCodeClass NOT_FILE = new ErrorCodeClass(10004, "不是文件");
    public static ErrorCodeClass NOT_MULTIPART_FROM_DATA = new ErrorCodeClass(10005, "不是multipart/form-data请求");

    //登录异常
    public static ErrorCodeClass USERNAME_NOT_EXISTS = new ErrorCodeClass(30001, "用户名不存在");
    public static ErrorCodeClass WRONG_PASSWORD = new ErrorCodeClass(30002, "密码错误");
    public static ErrorCodeClass EXPIRED_LOGIN = new ErrorCodeClass(30003, "登录状态已过期");
    public static ErrorCodeClass NULL_USERNAME = new ErrorCodeClass(30004, "用户名为空");
    public static ErrorCodeClass NULL_PASSWORD = new ErrorCodeClass(30005, "密码为空");
    public static ErrorCodeClass UNTRUSTED_IP = new ErrorCodeClass(30006, "不受信任的IP");
    public static ErrorCodeClass NOT_LOGGED_IN = new ErrorCodeClass(30007, "未登录");
    public static ErrorCodeClass HAD_LOGGED_IN = new ErrorCodeClass(30008, "已有登录用户");
    public static ErrorCodeClass NEED_VERIFY = new ErrorCodeClass(30009, "请求的权限需要验证");
    public static ErrorCodeClass WRONG_CAPTCHA = new ErrorCodeClass(30010, "验证码错误");
    public static ErrorCodeClass NULL_CAPTCHA = new ErrorCodeClass(30011, "验证码为空");
    public static ErrorCodeClass UNKNOWN_PASSWORD = new ErrorCodeClass(30012,"未知的密码串");

    //服务器异常
    public static ErrorCodeClass IO_EXCEPTION = new ErrorCodeClass(50001, "IO出现错误");
    public static ErrorCodeClass INITIALIZE_FAILED = new ErrorCodeClass(50003, "初始化系统出现错误");

    private final int code;
    private final Object data;

    public ErrorCodeClass(final int code, final Object data) {
        this.code = code;
        this.data = data;
    }


    public static ErrorCodeClass successData(Object object) {
        return new ErrorCodeClass(0, object);
    }

    @Override
    @JSONField(name = "code")
    public int getCode() {
        return code;
    }

    @Override
    @JSONField(name = "data")
    public Object getData() {
        return data;
    }

    @Override
    public boolean equals(ErrorCode other) {
        return code == other.getCode();
    }

    @Override
    public String toString() {
        return JSON.toJSONString(this, SerializerFeature.WriteMapNullValue);
    }

}