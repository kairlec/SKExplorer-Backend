package com.kairlec.contrller

import com.kairlec.`interface`.ResponseDataInterface
import com.kairlec.annotation.JsonRequestMapping
import com.kairlec.constant.ServiceErrorEnum
import com.kairlec.local.utils.*
import com.kairlec.local.utils.ResponseDataUtils.responseOK
import com.kairlec.model.bo.StartupConfig
import com.kairlec.model.vo.FileInfo
import com.kairlec.utils.*
import org.apache.logging.log4j.LogManager
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.multipart.MultipartFile
import org.springframework.web.multipart.MultipartHttpServletRequest
import java.nio.file.Files
import java.nio.file.Paths
import java.nio.file.StandardCopyOption
import java.util.*
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse


/**
 *@program: SKExplorer
 *@description: 文件API
 *@author: Kairlec
 *@create: 2020-03-08 18:11
 */
@JsonRequestMapping(value = ["/file"])
@RestController
class FileController {

    @Autowired
    private lateinit var startupConfig: StartupConfig

    @RequestMapping(value = ["/list"])
    fun file(request: HttpServletRequest): ResponseDataInterface {
        val sourcePath = request.getSourcePath()
        val realSourcePath = SKFileUtils.getContentPath(sourcePath)
        logger.info("获取到的路径为$realSourcePath")
        if (Files.notExists(realSourcePath)) {
            ServiceErrorEnum.FILE_NOT_EXISTS.throwout()
        }
        if (!Files.isDirectory(realSourcePath)) {
            ServiceErrorEnum.NOT_DIR.throwout()
        }
        val fileList = realSourcePath.getFileInfoList(startupConfig.excludeFile, startupConfig.excludeDir, startupConfig.excludeExt)
        return fileList.responseOK
    }

    @RequestMapping(value = ["/upload"])
    fun upload(request: HttpServletRequest): ResponseDataInterface {
        val multiRequest = request as MultipartHttpServletRequest
        logger.info("收到上传命令")
        val sourcePath = request.getSourcePath()
        val realSourcePath = SKFileUtils.getContentPath(sourcePath)
        val isReplace = request["replace"]?.toBoolean() ?: false
        val files: MutableList<MultipartFile> = ArrayList()
        val a = multiRequest.fileNames //返回的数量与前端input数量相同, 返回的字符串即为前端input标签的name
        while (a.hasNext()) {
            val name = a.next()
            val multipartFiles = multiRequest.getFiles(name) //获取单个input标签上传的文件，可能为多个
            files.addAll(multipartFiles)
        }
        val fileInfos = ArrayList<FileInfo>()
        files.forEach { file ->
            if (file.isEmpty) {
                ServiceErrorEnum.FILE_EMPTY.throwout()
            }
            var fileName = file.originalFilename
            if (fileName == null) {
                fileName = UUID.randomUUID().toString().replace("-", "")
            }
            val subFile = Paths.get(realSourcePath.toString(), fileName)
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

    @RequestMapping(value = ["/move"])
    fun move(request: HttpServletRequest): ResponseDataInterface {
        val sourcePath = request.getSourcePath()
        val targetPath = request.getTargetPath()
        val realSourcePath = SKFileUtils.getContentPath(sourcePath)
        val realTargetPath = SKFileUtils.getContentPath(targetPath)
        if (!Files.exists(realSourcePath)) {
            ServiceErrorEnum.FILE_NOT_EXISTS.throwout()
        }
        val replace = request["replace"]?.toBoolean() ?: false
        if (replace) {
            Files.move(realSourcePath, realTargetPath, StandardCopyOption.REPLACE_EXISTING)
        } else {
            Files.move(realSourcePath, realTargetPath)
        }
        return realTargetPath.toFile().fileInfo.responseOK
    }

    @RequestMapping(value = ["/delete"])
    fun delete(request: HttpServletRequest): ResponseDataInterface {
        val sourcePath = request.getSourcePath()
        val filePath = SKFileUtils.getContentPath(sourcePath)
        if (Files.notExists(filePath)) {
            ServiceErrorEnum.FILE_NOT_EXISTS.throwout()
        }
        Files.delete(filePath)
        return null.responseOK
    }

    @RequestMapping(value = ["/rename"])
    fun rename(request: HttpServletRequest): ResponseDataInterface {
        val sourcePath = request.getSourcePath()
        val targetName = request.getTargetPath()
        val realSourcePath = SKFileUtils.getContentPath(sourcePath)
        val realTargetPath = Paths.get(realSourcePath.parent.toString(), targetName)
        if (Files.notExists(realSourcePath)) {
            ServiceErrorEnum.FILE_NOT_EXISTS.throwout()
        }
        val replace = request["replace"]?.toBoolean() ?: false
        if (replace) {
            Files.move(realSourcePath, realTargetPath, StandardCopyOption.REPLACE_EXISTING)
        } else {
            Files.move(realSourcePath, realTargetPath)
        }
        return realTargetPath.toFile().fileInfo.responseOK
    }

    @RequestMapping(value = ["/download"])
    fun download(request: HttpServletRequest, response: HttpServletResponse) = MultipartFileSender.fromPath(SKFileUtils.getContentPath(request.getSourcePath()), request, response).serveResource()

    @RequestMapping(value = ["/create"])
    fun create(request: HttpServletRequest): ResponseDataInterface {
        val sourcePath = request.getSourcePath()
        val targetName = request.getTargetPath()
        val type = request["type"] ?: ServiceErrorEnum.MISSING_REQUIRED_PARAMETERS.throwout()
        val content = request["content"]
        val realSourcePath = SKFileUtils.getContentPath(sourcePath)
        val replace = request["replace"]?.toBoolean() ?: false

        when (type) {
            "file" -> {
                val realTargetPath = Paths.get(realSourcePath.parent.toString(), targetName)
                if (Files.exists(realTargetPath)) {
                    if (replace) {
                        Files.delete(realTargetPath)
                    } else {
                        ServiceErrorEnum.FILE_ALREADY_EXIST.throwout()
                    }
                }
                Files.createFile(realTargetPath)
                content?.let {
                    Files.writeString(realTargetPath, content, Charsets.UTF_8)
                }
                return realTargetPath.toFile().fileInfo.responseOK
            }
            "folder" -> {
                val realTargetPath = Paths.get(realSourcePath.parent.toString(), targetName)
                if (Files.exists(realTargetPath)) {
                    if (replace) {
                        Files.delete(realTargetPath)
                    } else {
                        ServiceErrorEnum.FILE_ALREADY_EXIST.throwout()
                    }
                }
                Files.createDirectory(realTargetPath)
                return realTargetPath.toFile().fileInfo.responseOK
            }
            "redirect" -> {
                val realTargetPath = Paths.get(realSourcePath.parent.toString(), "$targetName.Redirect")
                if (Files.exists(realTargetPath)) {
                    if (replace) {
                        Files.delete(realTargetPath)
                    } else {
                        ServiceErrorEnum.FILE_ALREADY_EXIST.throwout()
                    }
                }
                Files.createFile(realTargetPath)
                content.let {
                    Files.writeString(realTargetPath, content, Charsets.UTF_8)
                }
                return realTargetPath.toFile().fileInfo.responseOK
            }
            else -> {
                return ServiceErrorEnum.UNKNOWN_REQUEST
            }
        }
    }

    @RequestMapping(value = ["/content"], produces = ["text/plain"])
    fun content(request: HttpServletRequest): String {
        val sourcePath = request.getSourcePath()
        val filePath = SKFileUtils.getContentPath(sourcePath)
        if (Files.notExists(filePath)) {
            ServiceErrorEnum.FILE_NOT_EXISTS.throwout()
        }
        return filePath.toFile().content()
    }

    @RequestMapping(value = ["/verify"])
    fun verify(request: HttpServletRequest): ResponseDataInterface {
        val sourcePath = request.getSourcePath()
        var verifyAlgorithm: VerifyAlgorithmEnum? = null
        request["verifyAlgorithmCode"]?.toIntOrNull()?.let {
            verifyAlgorithm = VerifyAlgorithmEnum.getVerifyAlgorithmEnum(it)
        }
        request["verifyAlgorithm"]?.let { algorithm ->
            VerifyAlgorithmEnum.getVerifyAlgorithmEnum(algorithm)?.let {
                verifyAlgorithm = it
            }
        }
        verifyAlgorithm ?: ServiceErrorEnum.MISSING_REQUIRED_PARAMETERS.throwout()
        val realSourcePath = SKFileUtils.getContentPath(sourcePath)
        if (!Files.exists(realSourcePath)) {
            ServiceErrorEnum.FILE_NOT_EXISTS.throwout()
        }
        if (Files.isDirectory(realSourcePath)) {
            ServiceErrorEnum.NOT_FILE.throwout()
        }
        return object {
            val algorithm: String = verifyAlgorithm!!.algorithm
            val verifyCode = VerifyUtils.getFileVerifyAsHex(realSourcePath.toFile(), verifyAlgorithm!!)
        }.responseOK
    }

    companion object {
        private val logger = LogManager.getLogger(FileController::class.java)
    }
}
