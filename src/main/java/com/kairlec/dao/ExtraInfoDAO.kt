package com.kairlec.dao

import com.kairlec.config.startup.StartupConfigFactory
import com.kairlec.constant.ServiceErrorEnum
import com.kairlec.intf.DAOInitializeable
import com.kairlec.local.utils.ResponseDataUtils.responseOK
import com.kairlec.local.utils.fileInfo
import com.kairlec.model.bo.AbsolutePath
import com.kairlec.model.vo.ExtraInfo
import com.kairlec.model.vo.RelativePath
import com.kairlec.utils.LocalConfig.Companion.toJSON
import com.kairlec.utils.LocalConfig.Companion.toObject
import com.kairlec.utils.content
import org.apache.commons.io.FileUtils
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

    fun getExtraInfo(sourceFile: RelativePath): ExtraInfo? {
        val absolutePath = getExtraInfoAbsolutePath(sourceFile)
        return if (Files.notExists(absolutePath.path)) {
            null
        } else {
            absolutePath.path.toFile().content.toObject<ExtraInfo>()
        }
    }

    fun renameExtraInfo(sourceFile: RelativePath, name: String) {
        val sourceFileExtraInfo = getExtraInfoAbsolutePath(sourceFile).path
        if (Files.notExists(sourceFileExtraInfo)) {
            return
        }
        val realTargetPath = Paths.get(sourceFileExtraInfo.parent.toString(), name)
        if (Files.exists(realTargetPath)) {
            ServiceErrorEnum.FILE_ALREADY_EXIST.throwout()
        }
        if (Files.isDirectory(sourceFileExtraInfo)) {
            FileUtils.moveDirectory(sourceFileExtraInfo.toFile(), realTargetPath.toFile())
        } else {
            FileUtils.moveFile(sourceFileExtraInfo.toFile(), realTargetPath.toFile())
            realTargetPath.toFile().fileInfo.responseOK
        }
    }

    fun moveExtraInfo(sourceFile: RelativePath, targetDir: RelativePath, isReplace: Boolean) {
        val sourceFileExtraInfo = getExtraInfoAbsolutePath(sourceFile).path
        val targetDirExtraInfo = getExtraInfoAbsolutePath(targetDir).path
        if (Files.notExists(sourceFileExtraInfo)) {
            return
        }
        if (Files.exists(targetDirExtraInfo) && !Files.isDirectory(targetDirExtraInfo)) {
            return
        }
        val realTargetPath = Paths.get(targetDirExtraInfo.toString(), sourceFileExtraInfo.fileName.toString())
        if (!isReplace && Files.exists(realTargetPath)) {
            ServiceErrorEnum.FILE_ALREADY_EXIST.throwout()
        }
        FileUtils.copyToDirectory(sourceFileExtraInfo.toFile(), targetDirExtraInfo.toFile())
        FileUtils.deleteQuietly(sourceFileExtraInfo.toFile())
    }

    fun deleteExtraInfo(sourceDirOrFile:RelativePath){
        val sourceDirOrFileExtraInfo = getExtraInfoAbsolutePath(sourceDirOrFile).path
        if(Files.notExists(sourceDirOrFileExtraInfo)){
            return
        }
        FileUtils.deleteQuietly(sourceDirOrFileExtraInfo.toFile())
    }

    override fun daoInit() {
        configRootPath = Paths.get(StartupConfigFactory.Instance.dataDirPath.toString(), "ExtraInfoMaps")
        if (Files.notExists(configRootPath)) {
            Files.createDirectories(configRootPath)
        }
    }
}