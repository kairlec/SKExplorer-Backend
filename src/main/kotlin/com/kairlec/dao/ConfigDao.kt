package com.kairlec.dao

import com.kairlec.`interface`.DAOInitializeable
import com.kairlec.config.startup.StartupConfigFactory
import com.kairlec.local.utils.WatchDir
import com.kairlec.model.vo.Config
import com.kairlec.utils.LocalConfig.Companion.toObject
import kotlinx.coroutines.*
import org.apache.logging.log4j.LogManager
import org.springframework.stereotype.Component
import java.nio.file.*
import kotlin.system.exitProcess


/**
 * @program: SKExplorer
 * @description: 配置管理Dao层
 * @author: Kairlec
 * @create: 2020-03-16 17:52
 */

@Component
object ConfigDao : DAOInitializeable {
    private val logger = LogManager.getLogger(ConfigDao::javaClass)

    lateinit var Instance: Config
    private var readyEditCounts = 0
    private val configFilePath = Paths.get(StartupConfigFactory.Instance.dataDirPath.toString(), "config.json")

    fun readyEdit() {
        readyEditCounts++
    }

    override fun daoInit() {
        try {
            if (Files.notExists(configFilePath)) {
                Files.createDirectories(configFilePath.parent)
                Files.createFile(configFilePath)
                this.Instance = Config.Default
            } else {
                this.Instance = configFilePath.toFile().readText().toObject() ?: run {
                    logger.fatal("Failed to load config file!")
                    exitProcess(-1)
                }
                this.Instance.adminUserArray.forEach { it.encode() }
            }
        } catch (e: Exception) {
            logger.fatal("Failed to load or create config file! Cause ${e.message}", e)
            exitProcess(-1)
        }
        saveConfig()
        enableConfigWatch()
    }

    fun enableConfigWatch() {
        WatchDir.register(configFilePath) { path, kind ->
            if (readyEditCounts == 0) {
                when (kind) {
                    StandardWatchEventKinds.ENTRY_DELETE,
                    StandardWatchEventKinds.ENTRY_CREATE -> saveConfig()
                    StandardWatchEventKinds.ENTRY_MODIFY -> {
                        if (!reload()) {
                            logger.error("Failed reload config")
                        } else {
                            logger.info("Success reload config")
                        }
                    }
                }
            } else {
                readyEditCounts--
            }
        }
//        GlobalScope.async(Dispatchers.IO) {
//            while (true) {
//                try {
//                    FileSystems.getDefault().newWatchService().use { watchService ->
//                        configFilePath.parent.register(watchService,
//                                StandardWatchEventKinds.ENTRY_CREATE,
//                                StandardWatchEventKinds.ENTRY_MODIFY,
//                                StandardWatchEventKinds.ENTRY_DELETE
//                        )
//                        while (true) {
//                            val key = watchService.take()
//                            for (watchEvent in key.pollEvents()) {
//                                val watchEventPath: WatchEvent<*> = watchEvent ?: continue
//                                val filename: Path = watchEventPath.context() as Path
//                                if (readyEditCounts == 0) {
//                                    val isSameFile: Boolean = try {
//                                        Files.isSameFile(configFilePath, Paths.get(configFilePath.parent.toString(), filename.toString()))
//                                    } catch (e: Exception) {
//                                        e.printStackTrace()
//                                        false
//                                    }
//                                    if (isSameFile) {
//                                        val kind = watchEvent.kind()
//                                        if (kind == StandardWatchEventKinds.OVERFLOW) {
//                                            continue
//                                        }
//                                        //创建配置
//                                        if (kind == StandardWatchEventKinds.ENTRY_CREATE) {
//                                            saveConfig()
//                                        }
//                                        //修改配置
//                                        if (kind == StandardWatchEventKinds.ENTRY_MODIFY) {
//                                            if (!reload()) {
//                                                logger.error("Failed reload config")
//                                            } else {
//                                                logger.info("Success reload config")
//                                            }
//                                        }
//                                        //删除配置
//                                        if (kind == StandardWatchEventKinds.ENTRY_DELETE) {
//                                            saveConfig()
//                                        }
//                                    }
//                                } else {
//                                    readyEditCounts--
//                                }
//                                if (!key.reset()) {
//                                    break
//                                }
//                                delay(1000)
//                            }
//                        }
//                    }
//                } catch (e: Exception) {
//                    logger.error("Config watch error , will try again after 30 seconds! Cause ${e.message}", e)
//                    delay(30000)
//                }
//            }
//        }
    }

    fun saveConfig() {
        if (Files.notExists(configFilePath)) {
            Files.createDirectories(configFilePath.parent)
            Files.createFile(configFilePath)
        }
        configFilePath.toFile().writeText(Instance.json)
    }

    fun reload(): Boolean {
        return try {
            Instance = configFilePath.toFile().readText().toObject() ?: return false
            this.Instance.adminUserArray.forEach { it.encode() }
            readyEdit()
            saveConfig()
            true
        } catch (e: Exception) {
            logger.error(e.message, e)
            false
        }
    }


}