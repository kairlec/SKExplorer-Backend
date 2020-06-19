package com.kairlec.local.utils

import com.kairlec.config.startup.StartupConfigFactory
import com.kairlec.model.bo.AbsolutePath
import com.kairlec.model.vo.RelativePath
import org.apache.logging.log4j.LogManager
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths


object FileUtils {
    private val logger = LogManager.getLogger(FileUtils::class.java)
    private lateinit var contentPath: String

    fun updateContentPath(value: String) {
        val tmp = value.replace("""[\\\/]+""".toRegex(), "/")
        contentPath =
                if (tmp.endsWith('/'))
                    tmp.substring(0, tmp.length - 1)
                else {
                    tmp
                }
    }

    fun isRoot(path: Path) = Files.isSameFile(path, Paths.get(contentPath))

    fun packPath2Response(path: AbsolutePath): RelativePath {
        val result = path.path.toAbsolutePath().toString().replace("""[\\\/]+""".toRegex(), "/").substring(contentPath.length)
        return if (result.startsWith("/")) {
            RelativePath(result)
        } else {
            RelativePath("/$result")
        }
    }

    fun unpackResponsePath(relativePath: String): AbsolutePath {
        return AbsolutePath(Paths.get(StartupConfigFactory.Instance.contentDir, relativePath))
    }

    fun unpackResponsePath(relativePath: RelativePath): AbsolutePath {
        return AbsolutePath(Paths.get(StartupConfigFactory.Instance.contentDir, relativePath.path))
    }

    fun getLogPath(logPath: String, relativePath: String): Path {
        return Paths.get(logPath, relativePath)
    }
}
