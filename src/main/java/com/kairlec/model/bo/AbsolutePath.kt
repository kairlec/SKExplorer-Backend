package com.kairlec.model.bo

import com.kairlec.local.utils.FileUtils
import com.kairlec.model.vo.RelativePath
import java.nio.file.Path

data class AbsolutePath(
        val path: Path
){
    fun toContentRelativePath(): RelativePath {
        return FileUtils.packPath2Response(this)
    }
}