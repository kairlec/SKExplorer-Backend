package com.kairlec.local.utils

import com.kairlec.constant.ServiceErrorEnum
import com.kairlec.model.vo.FileInfo
import com.kairlec.local.utils.FileUtils.packPath2Response
import com.kairlec.model.bo.AbsolutePath
import com.kairlec.utils.LocalConfig
import org.apache.commons.io.FilenameUtils
import java.io.File


val File.fileInfo: FileInfo
    get() {
        val path = packPath2Response(AbsolutePath(this.toPath()))
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
        return FileInfo(name, type, size, editTime, path, LocalConfig.extraInfoServiceImpl.getExtraInfoDefault(path))
    }