package com.kairlec.utils.file


import com.kairlec.pojo.json.FileInfo;
import com.kairlec.pojo.json.FileList;
import java.io.IOException
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

object GetFileList {
    fun byPath(path: Path, contentPath: String, excludeFile: Array<String>, excludeDir: Array<String>, excludeExt: Array<String>): FileList {
        var isRoot: Boolean
        var localPath: String
        try {
            if (Files.isSameFile(path, Paths.get(contentPath))) {
                isRoot = true
                localPath = "/"
            } else {
                isRoot = false
                localPath = path.toString().substring(contentPath.length).replace("\\\\", "/")
            }
        } catch (e: IOException) {
            e.printStackTrace();
            isRoot = false
            localPath = path.toString().substring(contentPath.length).replace("\\\\", "/")
        }
        if (!localPath.startsWith("/")) {
            localPath = "/$localPath"
        }
        val fileList = FileList(isRoot, ArrayList(), localPath)
        val fileInfos = ArrayList<FileInfo>()
        val filePaths = ArrayList<String>()

        Files.newDirectoryStream(path).use { stream ->
            stream.forEach {
                if (Files.isDirectory(it)) {
                    val subDirName = it.fileName.toString()
                    if (subDirName !in excludeDir) {
                        GetFileInfo.ByPath(contentPath, it)?.let { fileInfo ->
                            fileInfos.add(fileInfo)
                            filePaths.add(fileInfo.path)
                        }
                    }
                } else {
                    val subFileName = it.fileName.toString()
                    //判断是否为排除名单文件
                    if (subFileName !in excludeFile) {
                        //判断是否为排除名单后缀
                        val ext = GetFileInfo.getExt(it)
                        if (ext !in excludeExt) {
                            GetFileInfo.ByPath(contentPath, it)?.let { fileInfo ->
                                fileInfos.add(fileInfo)
                                filePaths.add(fileInfo.path)
                            }
                        }
                    }
                }
            }
        }
        if (filePaths.isNotEmpty()) {
            GetFileInfo.MatchDescription(fileInfos, filePaths)
        }
        fileList.updateItems(fileInfos)
        return fileList
    }

}
