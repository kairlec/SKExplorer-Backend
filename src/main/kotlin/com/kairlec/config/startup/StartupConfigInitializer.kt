package com.kairlec.config.startup

import com.kairlec.`interface`.DAOInitializeable
import com.kairlec.model.bo.StartupConfig
import com.kairlec.utils.ClassUtils
import org.apache.logging.log4j.LogManager
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.ApplicationContext
import org.springframework.stereotype.Component
import javax.annotation.PostConstruct
import kotlin.reflect.full.createInstance

/**
 * @program: SKExplorer
 * @description: 程序启动配置
 * @author: Kairlec
 * @create: 2020-03-08 18:07
 * @suppress
 */

@Component
open class StartupConfigInitializer {

    @Autowired
    private lateinit var startupConfig: StartupConfig

    @Autowired
    private lateinit var applicationContext: ApplicationContext

    private val logger = LogManager.getLogger(StartupConfigInitializer::class.java)

    @PostConstruct
    fun init() {
        StartupConfigFactory.Instance = startupConfig
        StartupConfigFactory.applicationContext = applicationContext
        ClassUtils.scanClasses("com.kairlec.dao").forEach {
            if (DAOInitializeable::class.java.isAssignableFrom(it)) {
                val kc = it.kotlin
                val instance = kc.objectInstance ?: kc.createInstance()
                (instance as DAOInitializeable).daoInit()
            }
        }
    }

}