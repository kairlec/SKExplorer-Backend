package com.kairlec;

import com.kairlec.utils.RSACoder
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.runApplication
import org.springframework.boot.system.ApplicationHome
import org.springframework.core.io.FileSystemResource
import org.springframework.core.io.support.PropertiesLoaderUtils
import org.springframework.web.bind.annotation.RestController
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.util.*
import kotlin.system.exitProcess


/**
 * @suppress
 */
@EnableConfigurationProperties
@SpringBootApplication
open class SKExplorerApplication {
    companion object {
        private val logger = LogManager.getLogger(SKExplorerApplication::class.java)

        @JvmStatic
        fun main(args: Array<String>) {
            val configurableApplicationContext = runApplication<SKExplorerApplication>(*args)
            val configurableEnvironment = configurableApplicationContext.environment
        }
    }

}