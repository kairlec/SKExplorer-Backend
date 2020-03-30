package com.kairlec.local.utils


import com.kairlec.model.vo.FileInfo
import com.kairlec.model.vo.FileList
import com.kairlec.local.utils.FileUtils.isRoot
import com.kairlec.model.bo.AbsolutePath
import org.apache.commons.io.FilenameUtils
import java.nio.file.Files

fun AbsolutePath.getFileInfoList(excludeFile: Array<String>, excludeDir: Array<String>, excludeExt: Array<String>): FileList {
    val root = isRoot(this.path)
    val localPath = this.toContentRelativePath()
    val fileList = FileList(root, ArrayList(), localPath.path)
    val fileInfos = ArrayList<FileInfo>()

    Files.newDirectoryStream(this.path).use { stream ->
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
    fileList.items=fileInfos
    return fileList
}

