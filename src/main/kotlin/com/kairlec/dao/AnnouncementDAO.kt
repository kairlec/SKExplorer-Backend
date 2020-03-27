package com.kairlec.dao

import com.kairlec.`interface`.DAOInitializeable
import com.kairlec.config.startup.StartupConfigFactory
import org.springframework.stereotype.Component
import java.nio.file.Paths

@Component
object AnnouncementDAO : DAOInitializeable {
    private val configFilePath = Paths.get(StartupConfigFactory.Instance.dataDirPath.toString(),"announcement.json")
    override fun daoInit(){

    }
}