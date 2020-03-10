package com.kairlec.local.utils

import com.kairlec.config.startup.StartupConfig
import com.kairlec.utils.LocalConfig
import java.nio.file.Path
import java.nio.file.Paths

object SKFileUtils {
    fun getContentPath(relativePath: String): Path {
        return Paths.get(StartupConfig.contentDir + relativePath)
    }

    fun getLogPath(logPath: String, relativePath: String): Path {
        return Paths.get(logPath + relativePath)
    }

    fun getExt(path: Path): String? {
        val fileName = path.fileName.toString()
        val pos = fileName.lastIndexOf('.')
        return if (pos != -1) {
            fileName.substring(pos + 1)
        } else {
            null
        }
    }
}
