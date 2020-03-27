package com.kairlec.annotation

import org.springframework.core.annotation.AliasFor
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod

/**
 * @program: SKExplorer
 * @description: 默认返回类型为Json的RequestMapping
 * @author: Kairlec
 * @create: 2020-03-11 15:59
 * @suppress
 */

@Target(AnnotationTarget.FUNCTION, AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
@RequestMapping(produces = ["application/json"])
annotation class JsonRequestMapping(
        @get:AliasFor(annotation = RequestMapping::class, attribute = "name")
        val name: String = "",
        @get:AliasFor(annotation = RequestMapping::class, attribute = "value")
        val value: Array<String> = [],
        @get:AliasFor(annotation = RequestMapping::class, attribute = "path")
        val path: Array<String> = [],
        @get:AliasFor(annotation = RequestMapping::class, attribute = "method")
        val method: Array<RequestMethod> = [],
        @get:AliasFor(annotation = RequestMapping::class, attribute = "params")
        val params: Array<String> = [],
        @get:AliasFor(annotation = RequestMapping::class, attribute = "headers")
        val headers: Array<String> = [],
        @get:AliasFor(annotation = RequestMapping::class, attribute = "consumes")
        val consumes: Array<String> = [],
        @get:AliasFor(annotation = RequestMapping::class, attribute = "produces")
        val produces: Array<String> = []
)
