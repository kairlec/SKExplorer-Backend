package com.kairlec.local.utils

import com.kairlec.exception.ServiceErrorEnum
import com.kairlec.pojo.json.FileInfo
import com.kairlec.local.utils.FileUtils.warpPath2Response
import org.apache.commons.io.FilenameUtils
import java.io.File


val File.fileInfo: FileInfo
    get() {
        val path = warpPath2Response(this.toPath())
        if (!this.exists()) {
            ServiceErrorEnum.FILE_NOT_EXISTS.throwout()
        }
        var name = this.name
        var type: String
        if (this.isDirectory) {
            type = "folder"
        } else {
            type = FilenameUtils.getExtension(name)
            if (type.equals("Redirect", true)) {
                name = FilenameUtils.getBaseName(name)
                type = FilenameUtils.getExtension(name)
            }
        }
        val size = this.length()
        val editTime = this.lastModified()
        return FileInfo(name, type, size, editTime, path)
    }