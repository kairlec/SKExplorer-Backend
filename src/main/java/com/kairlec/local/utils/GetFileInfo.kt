package com.kairlec.local.utils

import com.kairlec.constant.ServiceErrorEnum
import com.kairlec.local.utils.FileUtils.packPath2Response
import com.kairlec.model.bo.AbsolutePath
import com.kairlec.model.vo.ExtraInfo
import com.kairlec.model.vo.FileInfo
import com.kairlec.utils.LocalConfig
import org.apache.commons.io.FilenameUtils
import java.io.File


val File.fileInfo: FileInfo
    get() {
        val path = packPath2Response(AbsolutePath(this.toPath()))
        if (!this.exists()) {
            ServiceErrorEnum.FILE_NOT_EXISTS.data(path.path).throwout()
        }
        val name = this.name
        val type: String
        val extraInfo: ExtraInfo
        if (this.isDirectory) {
            type = "folder"
            extraInfo = ExtraInfo.Default
        } else {
            type = FilenameUtils.getExtension(name)
            extraInfo = LocalConfig.extraInfoServiceImpl.getExtraInfoDefault(path)
        }
        val size = this.length()
        val editTime = this.lastModified()
        return FileInfo(name, type, size, editTime, path, extraInfo)
    }