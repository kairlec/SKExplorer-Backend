package com.kairlec

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.runApplication


/**
 * @suppress
 */
@EnableConfigurationProperties
@SpringBootApplication
open class SKExplorerApplication

fun main(args: Array<String>) {
    runApplication<SKExplorerApplication>(*args)
}