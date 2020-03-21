package com.kairlec.config.startup

import com.kairlec.dao.ConfigDao
import com.kairlec.pojo.StartupConfig
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.ApplicationContext
import org.springframework.stereotype.Component
import javax.annotation.PostConstruct

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


    @PostConstruct
    fun init() {
        StartupConfigFactory.Instance = startupConfig
        StartupConfigFactory.applicationContext = applicationContext
        ConfigDao.init()
    }

}