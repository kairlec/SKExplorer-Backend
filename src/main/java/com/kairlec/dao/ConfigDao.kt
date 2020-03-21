package com.kairlec.dao

import com.kairlec.config.startup.StartupConfigFactory
import com.kairlec.pojo.Config
import com.kairlec.utils.LocalConfig.Companion.toObject
import kotlinx.coroutines.*
import org.apache.logging.log4j.LogManager
import org.springframework.boot.SpringApplication
import org.springframework.stereotype.Component
import java.nio.file.*


/**
 * @program: SKExplorer
 * @description: 配置管理Dao层
 * @author: Kairlec
 * @create: 2020-03-16 17:52
 */

@Component
object ConfigDao {
    private val logger = LogManager.getLogger(ConfigDao::javaClass)

    lateinit var Instance: Config
    private var readyEditCounts = 0
    private val startupConfig
        get() = StartupConfigFactory.Instance


    private fun fatal(msg: String): Nothing {
        logger.fatal(msg)
        SpringApplication.exit(StartupConfigFactory.applicationContext)
        throw RuntimeException("System.exit returned normally, while it was supposed to halt JVM.")
    }

    fun readyEdit() {
        readyEditCounts++
    }

    fun init() {
        try {
            if (Files.notExists(startupConfig.configFilePath)) {
                Files.createDirectories(startupConfig.configFilePath.parent)
                Files.createFile(startupConfig.configFilePath)
                this.Instance = Config.Default
            } else {
                this.Instance = startupConfig.configFilePath.toFile().readText().toObject()
                        ?: fatal("Failed to load config file!")
                this.Instance.adminUserArray.forEach { it.encode() }
            }
        } catch (e: Exception) {
            e.printStackTrace()
            fatal("Failed to load or create config file!")
        }
        saveConfig()
        enableConfigWatch()
    }

    fun enableConfigWatch() {
        GlobalScope.async(Dispatchers.IO) {
            while (true) {
                try {
                    FileSystems.getDefault().newWatchService().use { watchService ->
                        startupConfig.configFilePath.parent.register(watchService,
                                StandardWatchEventKinds.ENTRY_CREATE,
                                StandardWatchEventKinds.ENTRY_MODIFY,
                                StandardWatchEventKinds.ENTRY_DELETE
                        )
                        while (true) {
                            val key = watchService.take()
                            for (watchEvent in key.pollEvents()) {
                                val watchEventPath: WatchEvent<*> = watchEvent ?: continue
                                val filename: Path = watchEventPath.context() as Path
                                if (readyEditCounts == 0) {
                                    val isSameFile: Boolean = try {
                                        Files.isSameFile(startupConfig.configFilePath, Paths.get(startupConfig.configFilePath.parent.toString(), filename.toString()))
                                    } catch (e: Exception) {
                                        e.printStackTrace()
                                        false
                                    }
                                    if (isSameFile) {
                                        val kind = watchEvent.kind()
                                        if (kind == StandardWatchEventKinds.OVERFLOW) {
                                            continue
                                        }
                                        //创建配置
                                        if (kind == StandardWatchEventKinds.ENTRY_CREATE) {
                                            saveConfig()
                                        }
                                        //修改配置
                                        if (kind == StandardWatchEventKinds.ENTRY_MODIFY) {
                                            if (!reload()) {
                                                logger.error("Failed reload config")
                                            } else {
                                                logger.info("Success reload config")
                                            }
                                        }
                                        //删除配置
                                        if (kind == StandardWatchEventKinds.ENTRY_DELETE) {
                                            saveConfig()
                                        }
                                    }
                                } else {
                                    readyEditCounts--
                                }
                                if (!key.reset()) {
                                    break
                                }
                                delay(1000)
                            }
                        }
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                    logger.error("Config watch error , will try again after 30 seconds")
                    delay(30000)
                }
            }
        }
    }

    fun saveConfig() {
        if (Files.notExists(startupConfig.configFilePath)) {
            Files.createDirectories(startupConfig.configFilePath.parent)
            Files.createFile(startupConfig.configFilePath)
        }
        startupConfig.configFilePath.toFile().writeText(Instance.json)
    }

    fun reload(): Boolean {
        return try {
            Instance = startupConfig.configFilePath.toFile().readText().toObject() ?: return false
            this.Instance.adminUserArray.forEach { it.encode() }
            readyEdit()
            saveConfig()
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }


}