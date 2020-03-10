package com.kairlec.annotation

/**
 *@program: SKExplorer
 *@description: API接口请求限制,防止恶意提交
 *@author: Kairlec
 *@create: 2020-02-27 13:03
 */

@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.FUNCTION)
annotation class RequestLimit(val seconds: Long, val maxCount: Long)