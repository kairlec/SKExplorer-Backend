package com.kairlec.dao

import com.kairlec.intf.DAOInitializeable
import com.kairlec.config.startup.StartupConfigFactory
import com.kairlec.model.vo.Announcement
import com.kairlec.utils.LocalConfig.Companion.toJSON
import com.kairlec.utils.LocalConfig.Companion.toObject
import com.kairlec.utils.content
import org.springframework.stereotype.Component
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths

@Component
object AnnouncementDAO : DAOInitializeable {
    private lateinit var configFilePath: Path

    fun delete(id: String) {
        val list = getAll()
        list.removeIf { it.id == id }
        save(list)
    }

    fun update(announcement: Announcement) {
        val list = getAll()
        list.find { it.id == announcement.id }?.let {
            it.content = announcement.content
            it.modifyTime = System.currentTimeMillis()
            save(list)
        }
    }

    fun addOrUpdate(announcement: Announcement) {
        val list = getAll()
        list.find { it.id == announcement.id }?.let {
            it.content = announcement.content
            it.modifyTime = System.currentTimeMillis()
        } ?: list.add(announcement)
        save(list)
    }

    fun add(content: String): Announcement {
        val list = getAll()
        var newAnnouncement = Announcement.newInstance(content, System.currentTimeMillis(), System.currentTimeMillis())
        while (true) {
            list.find { it.id == newAnnouncement.id }?.let { newAnnouncement = Announcement.newInstance(content, System.currentTimeMillis(), System.currentTimeMillis()) }
                    ?: break
        }
        list.add(newAnnouncement)
        save(list)
        return newAnnouncement
    }

    fun getAll(): ArrayList<Announcement> {
        return getAllContent().toObject<ArrayList<Announcement>>() ?: ArrayList()
    }

    fun getAllContent(): String {
        return configFilePath.toFile().content
    }

    private fun save(list: List<Announcement>) {
        Files.writeString(configFilePath, String.toJSON(list))
    }


    override fun daoInit() {
        configFilePath = Paths.get(StartupConfigFactory.Instance.dataDirPath.toString(), "announcement.json")
        if (Files.notExists(configFilePath)) {
            Files.createFile(configFilePath)
            Files.writeString(configFilePath, "[]")
        }
    }
}