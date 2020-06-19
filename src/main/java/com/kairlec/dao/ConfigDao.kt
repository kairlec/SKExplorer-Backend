package com.kairlec.dao

import com.kairlec.intf.DAOInitializeable
import com.kairlec.config.startup.StartupConfigFactory
import com.kairlec.local.utils.WatchDir
import com.kairlec.model.vo.Config
import com.kairlec.utils.LocalConfig.Companion.toObject
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
    private lateinit var configFilePath: Path

    fun readyEdit() {
        readyEditCounts++
    }

    override fun daoInit() {
        configFilePath = Paths.get(StartupConfigFactory.Instance.dataDirPath.toString(), "config.json")
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
        WatchDir.register(configFilePath) { _, kind ->
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