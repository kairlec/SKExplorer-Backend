package com.kairlec.utils.file

import com.kairlec.pojo.json.FileInfo
import com.kairlec.utils.LocalConfig
import java.io.File
import java.nio.file.Path


object GetFileInfo {
    fun getExt(path: Path): String? {
        val fileName = path.fileName.toString()
        val pos = fileName.lastIndexOf('.')
        return if (pos != -1) {
            fileName.substring(pos + 1)
        } else {
            null
        }
    }

    fun getExt(fileName: String): String? {
        val pos = fileName.lastIndexOf('.')
        return if (pos != -1) {
            fileName.substring(pos + 1)
        } else {
            null
        }
    }

    fun MatchDescription(fileInfos: List<FileInfo>, Paths: List<String>) {
        val descriptionMapList = LocalConfig.descriptionMapService.getSection(Paths) ?: return
        for (descriptionMap in descriptionMapList) {
            for (fileInfo in fileInfos) {
                if (fileInfo.path == descriptionMap.path) {
                    fileInfo.description = descriptionMap.description
                    break
                }
            }
        }
    }

    fun ByPath(root: String, path: Path): FileInfo? {
        return ByFile(root, path.toFile())
    }

    fun ByFile(root: String, file: File): FileInfo? {
        val path = file.path.replace(root, "").replace("\\", "/")
        val exist: Boolean
        if (file.exists()) {
            exist = true
        } else {
            exist = false
            return null
        }
        var name = file.name
        var type: String?
        if (file.isDirectory) {
            type = "folder"
        } else {
            type = getExt(name)
            if (type == "Redirect") {
                name = name.substring(0, name.lastIndexOf('.'))
                type = getExt(name)
            }
        }
        val size = file.length()
        val editTime = file.lastModified()
        return FileInfo(name, type, size, editTime, null, path, exist)
    }
}
