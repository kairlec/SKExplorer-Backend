package com.kairlec.service.impl

import com.kairlec.constant.ServiceErrorEnum
import com.kairlec.intf.ResponseDataInterface
import com.kairlec.local.utils.MultipartFileSender
import com.kairlec.local.utils.ResponseDataUtils.responseOK
import com.kairlec.local.utils.fileInfo
import com.kairlec.local.utils.getFileInfoList
import com.kairlec.model.bo.AbsolutePath
import com.kairlec.model.bo.StartupConfig
import com.kairlec.model.vo.ExtraInfo
import com.kairlec.model.vo.FileInfo
import com.kairlec.model.vo.RelativePath
import com.kairlec.service.FileService
import com.kairlec.utils.LocalConfig
import com.kairlec.utils.VerifyAlgorithmEnum
import com.kairlec.utils.VerifyUtils
import com.kairlec.utils.content
import org.apache.commons.io.FileUtils
import org.apache.logging.log4j.LogManager
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import java.io.BufferedInputStream
import java.io.IOException
import java.net.URL
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import java.util.*
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@Service
class FileServiceImpl : FileService {
    @Autowired
    private lateinit var startupConfig: StartupConfig

    @Autowired
    private lateinit var extraInfoServiceImpl: ExtraInfoServiceImpl

    private fun list(sourceFile: Path): ResponseDataInterface {
        if (Files.notExists(sourceFile)) {
            ServiceErrorEnum.FILE_NOT_EXISTS.throwout()
        }
        if (!Files.isDirectory(sourceFile)) {
            ServiceErrorEnum.NOT_DIR.throwout()
        }
        val fileList = AbsolutePath(sourceFile).getFileInfoList(startupConfig.excludeFile, startupConfig.excludeDir, startupConfig.excludeExt)
        return fileList.responseOK
    }

    override fun list(sourceDir: RelativePath): ResponseDataInterface {
        val sourceDirContent = sourceDir.toContentAbsolutePath().path
        return list(sourceDirContent)
    }

    override fun upload(parts: List<MultipartFile>, sourceDir: RelativePath, isReplace: Boolean): ResponseDataInterface {
        val sourceDirContent = sourceDir.toContentAbsolutePath().path
        logger.info("收到上传命令")
        val fileInfos = ArrayList<FileInfo>()
        parts.forEach { file ->
            if (file.isEmpty) {
                ServiceErrorEnum.FILE_EMPTY.throwout()
            }
            var fileName = file.originalFilename
            if (fileName == null) {
                fileName = UUID.randomUUID().toString().replace("-", "")
            }
            val subFile = Paths.get(sourceDirContent.toString(), fileName)
            if (!isReplace && Files.exists(subFile)) {
                logger.error(subFile.toString() + "已存在")
                ServiceErrorEnum.FILE_ALREADY_EXIST.throwout()
            }
            file.transferTo(subFile.toFile())
            fileInfos.add(subFile.toFile().fileInfo)
            logger.info(subFile.toString() + "成功")
        }
        return fileInfos.responseOK
    }

    override fun move(sourceDirOrFile: RelativePath, targetDir: RelativePath, isReplace: Boolean): ResponseDataInterface {
        val sourceDirOrFileContent = sourceDirOrFile.toContentAbsolutePath().path
        val targetDirContent = targetDir.toContentAbsolutePath().path
        if (!Files.exists(sourceDirOrFileContent)) {
            ServiceErrorEnum.FILE_NOT_EXISTS.throwout()
        }
        if (Files.exists(targetDirContent) && !Files.isDirectory(targetDirContent)) {
            ServiceErrorEnum.NOT_DIR.throwout()
        }
        val realTargetPath = Paths.get(targetDirContent.toString(), sourceDirOrFileContent.fileName.toString())
        if (!isReplace && Files.exists(realTargetPath)) {
            ServiceErrorEnum.FILE_ALREADY_EXIST.throwout()
        }
        FileUtils.copyToDirectory(sourceDirOrFileContent.toFile(), targetDirContent.toFile())
        FileUtils.deleteQuietly(sourceDirOrFileContent.toFile())
        extraInfoServiceImpl.moveExtraInfo(sourceDirOrFile, targetDir, isReplace)
        return if (Files.isDirectory(realTargetPath)) {
            list(realTargetPath)
        } else {
            realTargetPath.toFile().fileInfo.responseOK
        }
    }

    override fun delete(sourceDirOrFile: RelativePath): ResponseDataInterface {
        val sourceDirOrFileContent = sourceDirOrFile.toContentAbsolutePath().path
        if (!Files.exists(sourceDirOrFileContent)) {
            ServiceErrorEnum.FILE_NOT_EXISTS.throwout()
        }
        FileUtils.deleteQuietly(sourceDirOrFileContent.toFile())
        extraInfoServiceImpl.deleteExtraInfo(sourceDirOrFile)
        return null.responseOK
    }

    override fun rename(sourceDirOrFile: RelativePath, targetName: String): ResponseDataInterface {
        val sourceDirOrFileContent = sourceDirOrFile.toContentAbsolutePath().path
        if (Files.notExists(sourceDirOrFileContent)) {
            ServiceErrorEnum.FILE_NOT_EXISTS.throwout()
        }
        val realTargetPath = Paths.get(sourceDirOrFileContent.parent.toString(), targetName)
        if (Files.exists(realTargetPath)) {
            ServiceErrorEnum.FILE_ALREADY_EXIST.throwout()
        }
        return if (Files.isDirectory(sourceDirOrFileContent)) {
            FileUtils.moveDirectory(sourceDirOrFileContent.toFile(), realTargetPath.toFile())
            extraInfoServiceImpl.renameExtraInfo(sourceDirOrFile, targetName)
            list(realTargetPath)
        } else {
            FileUtils.moveFile(sourceDirOrFileContent.toFile(), realTargetPath.toFile())
            extraInfoServiceImpl.renameExtraInfo(sourceDirOrFile, targetName)
            realTargetPath.toFile().fileInfo.responseOK
        }
    }

    override fun download(sourceFile: RelativePath, request: HttpServletRequest, response: HttpServletResponse) {
        if (LocalConfig.configServiceImpl.getSystemConfig().redirectEnable) {
            val extraInfo = extraInfoServiceImpl.getExtraInfoDefault(sourceFile)
            if (extraInfo.redirect) {
                response.sendRedirect(extraInfo.redirectContent)
            }
        }
        MultipartFileSender.fromPath(sourceFile.toContentAbsolutePath().path, request, response).serveResource()
    }

    override fun create(sourceDir: RelativePath, targetName: String, type: String, content: String?): ResponseDataInterface {
        val realTargetPath = Paths.get(sourceDir.toContentAbsolutePath().path.parent.toString(), targetName)
        if (Files.exists(realTargetPath)) {
            ServiceErrorEnum.FILE_ALREADY_EXIST.throwout()
        }
        when (type) {
            "file" -> {
                Files.createFile(realTargetPath)
                content?.let {
                    Files.writeString(realTargetPath, content, Charsets.UTF_8)
                }
                return realTargetPath.toFile().fileInfo.responseOK
            }
            "folder" -> {
                Files.createDirectory(realTargetPath)
                return list(realTargetPath)
            }
            else -> {
                return ServiceErrorEnum.UNKNOWN_REQUEST
            }
        }
    }

    override fun content(sourceFile: RelativePath): String {
        val sourceFileContent = sourceFile.toContentAbsolutePath().path
        if (Files.notExists(sourceFileContent)) {
            ServiceErrorEnum.FILE_NOT_EXISTS.throwout()
        }
        return sourceFileContent.toFile().content
    }

    override fun extraInfo(sourceFile: RelativePath): ResponseDataInterface {
        return extraInfoServiceImpl.getExtraInfoDefault(sourceFile).responseOK
    }

    override fun updateExtraInfo(sourceFile: RelativePath, extraInfo: ExtraInfo): ResponseDataInterface {
        extraInfoServiceImpl.setExtraInfo(sourceFile, extraInfo)
        return extraInfo.responseOK
    }

    override fun remoteDownload(sourceFile: RelativePath, url: URL): ResponseDataInterface {
        val sourceFileContent = sourceFile.toContentAbsolutePath().path
        if (Files.notExists(sourceFileContent)) {
            if (Files.notExists(sourceFileContent.parent)) {
                Files.createDirectories(sourceFileContent.parent)
            }
            Files.createFile(sourceFileContent)
        }
        try {
            url.openStream().use { urlStream ->
                BufferedInputStream(urlStream).use { bufferInputStream ->
                    Files.newOutputStream(sourceFileContent).use { outputStream ->
                        val arr = ByteArray(8192)
                        var len: Int
                        while (bufferInputStream.read(arr).also { len = it } != -1) {
                            outputStream.write(arr, 0, len)
                            outputStream.flush()
                        }
                    }
                }
            }
        } catch (e: IOException) {
            logger.error("Failed to download file:${url}", e)
        }
        return sourceFileContent.toFile().fileInfo.responseOK
    }

    override fun verify(sourceFile: RelativePath, verifyAlgorithm: VerifyAlgorithmEnum): ResponseDataInterface {
        val sourceFileContent = sourceFile.toContentAbsolutePath().path
        if (!Files.exists(sourceFileContent)) {
            ServiceErrorEnum.FILE_NOT_EXISTS.throwout()
        }
        if (Files.isDirectory(sourceFileContent)) {
            ServiceErrorEnum.NOT_FILE.throwout()
        }
        return object {
            val algorithm: String = verifyAlgorithm.algorithm
            val verifyCode = VerifyUtils.getFileVerifyAsHex(sourceFileContent.toFile(), verifyAlgorithm)
        }.responseOK
    }

    companion object {
        private val logger = LogManager.getLogger(FileServiceImpl::class.java)
    }
}