package com.kairlec.config.startup

import com.kairlec.pojo.StartupConfig
import com.kairlec.local.utils.FileUtils
import org.apache.logging.log4j.LogManager
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.SpringBootConfiguration
import org.springframework.context.ApplicationContext
import org.springframework.context.annotation.Bean
import org.springframework.core.env.get
import java.io.File
import java.nio.file.InvalidPathException
import java.nio.file.Path
import kotlin.system.exitProcess

/**
 * @program: SKExplorer
 * @description: 程序启动配置
 * @author: Kairlec
 * @create: 2020-03-08 18:07
 * @suppress
 */

@SpringBootConfiguration
open class StartupConfigFactory {

    @Autowired
    private lateinit var applicationContext: ApplicationContext

    @Bean(name = ["startupConfig"])
    open fun startupConfigMaker(): StartupConfig {
        val env = applicationContext.environment
        var contentDir = env["content"] ?: run {
            logger.fatal("未配置content")
            exitProcess(-1)
        }
        if (!contentDir.endsWith(File.separator)) {
            contentDir += File.separator
        }
        FileUtils.updateContentPath(contentDir)
        val excludeDir = env["excludedir"]?.split('/')?.toTypedArray() ?: run {
            logger.warn("未配置排除文件夹或格式错误")
            emptyArray<String>()
        }
        val excludeFile = env["excludefile"]?.split('/')?.toTypedArray() ?: run {
            logger.warn("未配置排除文件或格式错误")
            emptyArray<String>()
        }
        val excludeExt = env["excludeext"]?.split('/')?.toTypedArray() ?: run {
            logger.warn("未配置排除文件后缀或格式错误")
            emptyArray<String>()
        }
        val captchaCount = env["captchacount"]?.toIntOrNull() ?: run {
            logger.warn("未配置验证码数或格式错误")
            4
        }
        val configFilePathString = env["config"] ?: "data/config.json"
        val configFilePath: Path
        try {
            configFilePath = Path.of(configFilePathString)
        } catch (e: InvalidPathException) {
            e.printStackTrace()
            logger.fatal("config配置了无效的路径:$configFilePathString")
            exitProcess(-1)
        }
        return StartupConfig(contentDir, excludeDir, excludeFile, excludeExt, captchaCount, configFilePath)
    }


    companion object {
        private val logger = LogManager.getLogger(StartupConfigFactory::class.java)
        lateinit var Instance: StartupConfig
        lateinit var applicationContext: ApplicationContext
    }

}