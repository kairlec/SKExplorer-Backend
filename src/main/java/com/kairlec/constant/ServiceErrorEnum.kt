package com.kairlec.constant

import com.fasterxml.jackson.annotation.JsonFormat
import com.fasterxml.jackson.annotation.JsonIgnore
import com.kairlec.`interface`.ResponseDataInterface
import java.io.ByteArrayOutputStream
import java.io.PrintWriter

/**
 *@program: SKExplorer
 *@description: 业务错误码
 *@author: Kairlec
 *@create: 2020-03-08 18:16
 */
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
enum class ServiceErrorEnum(override val code: Int, override val msg: String, override var data: Any? = null) : ResponseDataInterface {
    //无异常
    NO_ERROR(0, "OK"),

    //未指名的异常
    UNSPECIFIED(90001, "未指名的错误"),
    UNKNOWN(90002, "未知错误"),
    AN_EXCEPTION_OCCURRED(90003, "发生了一个异常"),

    //请求异常
    UNKNOWN_REQUEST(400, "未知的请求"),
    FILE_NOT_EXISTS(404, "文件不存在"),
    NO_CONTENT(204, "无内容可显示"),
    INVALID_DIR(10001, "无效的文件夹"),
    INVALID_FILE(10002, "无效的文件"),
    NOT_DIR(10003, "不是文件夹"),
    NOT_FILE(10004, "不是文件"),
    NOT_MULTIPART_FROM_DATA(10005, "不是multipart/form-data请求"),
    MISSING_REQUIRED_PARAMETERS(10006, "缺少必要的参数"),
    FILE_EMPTY(10007, "文件为空"),
    FILE_ALREADY_EXIST(10008, "文件已存在"),

    //登录异常
    USERNAME_NOT_EXISTS(30001, "用户名不存在"),
    WRONG_PASSWORD(30002, "密码错误"),
    EXPIRED_LOGIN(30003, "登录状态已过期"),
    NULL_USERNAME(30004, "用户名为空"),
    NULL_PASSWORD(30005, "密码为空"),
    UNTRUSTED_IP(30006, "不受信任的IP"),
    NOT_LOGGED_IN(30007, "未登录"),
    HAD_LOGGED_IN(30008, "已有登录用户"),
    NEED_VERIFY(30009, "请求的权限需要验证"),
    WRONG_CAPTCHA(30010, "验证码错误"),
    NULL_CAPTCHA(30011, "验证码为空"),
    UNKNOWN_PASSWORD(30012, "未知的密码串"),

    //服务器异常
    IO_EXCEPTION(50001, "IO出现错误"),
    INITIALIZE_FAILED(50003, "初始化系统出现错误"),

    ;

    fun data(data: Any?): ServiceErrorEnum {
        this.data = data
        return this
    }

    @JsonIgnore
    val ok = code == 0

    @JsonIgnore
    val bad = code != 0

    fun throwout(): Nothing = throwout(this)

    companion object {
        fun throwout(error: ServiceErrorEnum): Nothing = throw SKException(error)

        fun fromException(e: Exception): ServiceErrorEnum {
            lateinit var expMessage: String
            ByteArrayOutputStream().use {
                e.printStackTrace(PrintWriter(it, true))
                expMessage = it.toString()
            }
            return AN_EXCEPTION_OCCURRED.data(expMessage)
        }
    }

}