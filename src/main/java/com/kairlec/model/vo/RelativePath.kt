package com.kairlec.model.vo

import com.fasterxml.jackson.databind.annotation.JsonSerialize
import com.kairlec.local.jackson.RelativePathSerializer
import com.kairlec.local.utils.FileUtils
import com.kairlec.model.bo.AbsolutePath

@JsonSerialize(using = RelativePathSerializer::class)
data class RelativePath(
        val path: String
) {
    fun toContentAbsolutePath(): AbsolutePath {
        return FileUtils.unpackResponsePath(this)
    }
}