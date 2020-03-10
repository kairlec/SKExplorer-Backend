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


@RestController
@EnableConfigurationProperties
@SpringBootApplication
open class SKExplorerApplication {
    companion object {
        private val logger: Logger = LogManager.getLogger(SKExplorerApplication::class.java)

        private fun init() {

            //加载application.properties
            logger.info("Init ...")
            val path = ApplicationHome(SKExplorerApplication::class.java).source.parentFile.toString()
            val aPath = "$path${File.separator}application.properties";
            logger.info("Path is $aPath")
            val resource = FileSystemResource(aPath)
            logger.info("Load file system resource completed")
            val properties: Properties = PropertiesLoaderUtils.loadProperties(resource)
            logger.info("Load properties completed")

            //检查是否有RSA公钥和密钥
            val publicKey = properties.getProperty("publickey", "")
            val privateKey = properties.getProperty("privatekey", "")
            if (publicKey.isEmpty() || privateKey.isEmpty()) {
                logger.info("Publickey or privatekey is not exist , create new publickey and privatekey")
                val key = RSACoder.initKey()
                properties.setProperty("publickey", RSACoder.getPublicKey(key))
                properties.setProperty("privatekey", RSACoder.getPrivateKey(key))
            }
            try {
                FileOutputStream(resource.file).use {
                    //properties.store 方法会转义:和=,所以这里使用直接保存
                    //properties.store(it, "new key")
                    val e = properties.propertyNames()
                    while (e.hasMoreElements()) {
                        val key = e.nextElement() as String
                        val value = properties.getProperty(key).replace("\\","\\\\")
                        val s = "$key=$value${System.lineSeparator()}"
                        it.write(s.toByteArray())
                    }
                    it.flush()
                }
            } catch (e: IOException) {
                logger.fatal("Initialize RSA code failed")
                e.printStackTrace()
                exitProcess(-1)
            }

            //检查是否设置了数据库连接项
            val databaseUrl = properties.getProperty("spring.datasource.url", "")
            val databaseUsername = properties.getProperty("spring.datasource.username", "")
            val databasePassword = properties.getProperty("spring.datasource.password", "")
            if (databaseUrl.isEmpty() || databaseUsername.isEmpty() || databasePassword.isEmpty()) {
                logger.fatal("Database login info is not exists , please set \"spring.datasource.url\",\"spring.datasource.username\",\"spring.datasource.password\"")
                exitProcess(-1)
            }

        }

        @JvmStatic
        fun main(args: Array<String>) {
            init()

            runApplication<SKExplorerApplication>(*args)
        }
    }

}