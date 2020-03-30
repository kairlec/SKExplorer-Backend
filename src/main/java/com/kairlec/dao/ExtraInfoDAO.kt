package com.kairlec.dao

import com.kairlec.config.startup.StartupConfigFactory
import com.kairlec.intf.DAOInitializeable
import com.kairlec.model.bo.AbsolutePath
import com.kairlec.model.vo.ExtraInfo
import com.kairlec.model.vo.RelativePath
import com.kairlec.utils.LocalConfig.Companion.toJSON
import com.kairlec.utils.LocalConfig.Companion.toObject
import com.kairlec.utils.content
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths

object ExtraInfoDAO : DAOInitializeable {
    private lateinit var configRootPath: Path

    private fun getExtraInfoAbsolutePath(path: RelativePath): AbsolutePath {
        return AbsolutePath(Paths.get(configRootPath.toString(), path.path))
    }

    private fun createFileIfNotExist(path: Path) {
        if (Files.notExists(path)) {
            val parent = path.parent
            if (Files.notExists(parent)) {
                Files.createDirectories(parent)
            }
            Files.createFile(path)
        }
    }

    fun updateOrCreateExtraInfo(path: RelativePath, extraInfo: ExtraInfo) {
        val absolutePath = getExtraInfoAbsolutePath(path)
        createFileIfNotExist(absolutePath.path)
        Files.writeString(absolutePath.path, String.toJSON(extraInfo), Charsets.UTF_8)
    }

    fun getExtraInfo(path: RelativePath): ExtraInfo? {
        val absolutePath = getExtraInfoAbsolutePath(path)
        return if (Files.notExists(absolutePath.path)) {
            null
        } else {
            absolutePath.path.toFile().content.toObject<ExtraInfo>()
        }
    }

    override fun daoInit() {
        configRootPath = Paths.get(StartupConfigFactory.Instance.dataDirPath.toString(), "ExtraInfoMaps")
        if (Files.notExists(configRootPath)) {
            Files.createDirectories(configRootPath)
        }
    }
}