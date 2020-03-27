package com.kairlec.local.utils

import com.kairlec.config.startup.StartupConfigFactory
import java.nio.file.Path
import java.nio.file.Paths

object SKFileUtils {

    fun getContentPath(relativePath: String): Path {
        return Paths.get(StartupConfigFactory.Instance.contentDir + relativePath)
    }

    fun getLogPath(logPath: String, relativePath: String): Path {
        return Paths.get(logPath + relativePath)
    }

}
