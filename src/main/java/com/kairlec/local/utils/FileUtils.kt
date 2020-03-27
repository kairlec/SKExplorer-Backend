package com.kairlec.local.utils

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

    fun warpPath2Response(path: Path): String {
        val result = path.toAbsolutePath().toString().replace("""[\\\/]+""".toRegex(), "/").substring(contentPath.length)
        return if (result.startsWith("/")) {
            result
        } else {
            "/$result"
        }
    }
}
