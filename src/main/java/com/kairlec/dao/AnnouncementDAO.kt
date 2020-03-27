package com.kairlec.dao

import com.kairlec.`interface`.DAOInitializeable
import com.kairlec.config.startup.StartupConfigFactory
import org.springframework.stereotype.Component
import java.nio.file.Path
import java.nio.file.Paths

@Component
object AnnouncementDAO : DAOInitializeable {
    private lateinit var configFilePath: Path
    override fun daoInit() {
        configFilePath = Paths.get(StartupConfigFactory.Instance.dataDirPath.toString(), "announcement.json")
    }
}