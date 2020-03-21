package com.kairlec.local.utils


import com.kairlec.pojo.json.FileInfo
import com.kairlec.pojo.json.FileList
import com.kairlec.local.utils.FileUtils.isRoot
import com.kairlec.local.utils.FileUtils.warpPath2Response
import org.apache.commons.io.FilenameUtils
import java.nio.file.Files
import java.nio.file.Path

fun Path.getFileInfoList(excludeFile: Array<String>, excludeDir: Array<String>, excludeExt: Array<String>): FileList {
    val root = isRoot(this)
    val localPath = warpPath2Response(this)
    val fileList = FileList(root, ArrayList(), localPath)
    val fileInfos = ArrayList<FileInfo>()

    Files.newDirectoryStream(this).use { stream ->
        stream.forEach {
            if (Files.isDirectory(it)) {
                val subDirName = it.fileName.toString()
                if (subDirName !in excludeDir) {
                    fileInfos.add(it.toFile().fileInfo)
                }
            } else {
                val subFileName = it.fileName.toString()
                //判断是否为排除名单文件
                if (subFileName !in excludeFile) {
                    //判断是否为排除名单后缀
                    val ext = FilenameUtils.getExtension(subFileName)
                    if (ext !in excludeExt) {
                        fileInfos.add(it.toFile().fileInfo)
                    }
                }
            }
        }
    }
    fileList.updateItems(fileInfos)
    return fileList
}

