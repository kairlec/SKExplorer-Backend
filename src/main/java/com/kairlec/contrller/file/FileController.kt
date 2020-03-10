package com.kairlec.contrller.file

import com.kairlec.config.startup.StartupConfig
import com.kairlec.exception.ServiceErrorEnum
import com.kairlec.local.utils.MultipartFileSender
import com.kairlec.local.utils.RequestUtils
import com.kairlec.local.utils.ResponseDataUtils
import com.kairlec.local.utils.SKFileUtils
import com.kairlec.pojo.json.FileInfo
import com.kairlec.utils.file.GetFileContent
import com.kairlec.utils.file.GetFileInfo
import com.kairlec.utils.file.GetFileList
import org.apache.logging.log4j.LogManager
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.multipart.MultipartFile
import org.springframework.web.multipart.MultipartHttpServletRequest
import java.io.BufferedReader
import java.io.InputStreamReader
import java.nio.charset.StandardCharsets
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
@RequestMapping("/file")
@RestController
class FileController {
    @RequestMapping(value = ["/list"])
    fun file(request: HttpServletRequest): String {
        val sourcePath = RequestUtils.getSourcePath(request)
                ?: ServiceErrorEnum.MISSING_REQUIRED_PARAMETERS.throwout()
        val realSourcePath = SKFileUtils.getContentPath(sourcePath)
        logger.info("获取到的路径为$realSourcePath")
        if (Files.notExists(realSourcePath)) {
            ServiceErrorEnum.FILE_NOT_EXISTS.throwout()
        }
        if (!Files.isDirectory(realSourcePath)) {
            ServiceErrorEnum.NOT_DIR.throwout()
        }
        val fileList = GetFileList.byPath(realSourcePath, StartupConfig.contentDir, StartupConfig.excludeFile, StartupConfig.excludeDir, StartupConfig.excludeExt)
        return ResponseDataUtils.ok(fileList)
    }

    @RequestMapping(value = ["/upload"])
    fun upload(request: HttpServletRequest): String {
        val multiRequest = request as MultipartHttpServletRequest
        logger.info("收到上传命令")
        val sourcePath = RequestUtils.getSourcePath(request)
                ?: ServiceErrorEnum.MISSING_REQUIRED_PARAMETERS.throwout()
        val realSourcePath = SKFileUtils.getContentPath(sourcePath)
        val isReplace = request.getParameter("replace")?.toBoolean() ?: false
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
                logger.warn(subFile.toString() + "已存在")
                ServiceErrorEnum.FILE_ALREADY_EXIST.throwout()
            }
            file.transferTo(subFile.toFile())
            GetFileInfo.ByPath(StartupConfig.contentDir, subFile)?.let {
                fileInfos.add(it)
            }
            logger.info(subFile.toString() + "成功")
        }
        return ResponseDataUtils.ok(fileInfos)
    }

    @RequestMapping(value = ["/move"])
    fun move(request: HttpServletRequest): String {
        val sourcePath = RequestUtils.getSourcePath(request)
        val targetPath = RequestUtils.getTargetPath(request)
        if (sourcePath == null || targetPath == null) {
            ServiceErrorEnum.MISSING_REQUIRED_PARAMETERS.throwout()
        }
        val realSourcePath = SKFileUtils.getContentPath(sourcePath)
        val realTargetPath = SKFileUtils.getContentPath(targetPath)
        if (!Files.exists(realSourcePath)) {
            ServiceErrorEnum.FILE_NOT_EXISTS.throwout()
        }
        val replace = request.getParameter("replace")
        if (replace.equals("true", ignoreCase = true)) {
            Files.move(realSourcePath, realTargetPath, StandardCopyOption.REPLACE_EXISTING)
        } else {
            Files.move(realSourcePath, realTargetPath)
        }
        return ResponseDataUtils.ok()
    }

    @RequestMapping(value = ["/delete"])
    fun delete(request: HttpServletRequest): String {
        val sourcePath = RequestUtils.getSourcePath(request)
                ?: ServiceErrorEnum.MISSING_REQUIRED_PARAMETERS.throwout()
        val filePath = SKFileUtils.getContentPath(sourcePath)
        if (!Files.exists(filePath)) {
            ServiceErrorEnum.FILE_NOT_EXISTS.throwout()
        }
        Files.delete(filePath)
        return ResponseDataUtils.ok()
    }

    @RequestMapping(value = ["/rename"])
    fun rename(request: HttpServletRequest?): String {
        //TODO 重命名文件
        return ResponseDataUtils.error(ServiceErrorEnum.UNKNOWN)
    }

    @RequestMapping(value = ["/download"])
    fun download(request: HttpServletRequest, response: HttpServletResponse) {
        val sourcePath = RequestUtils.getSourcePath(request) ?: ServiceErrorEnum.MISSING_REQUIRED_PARAMETERS.throwout()
        MultipartFileSender.fromPath(SKFileUtils.getContentPath(sourcePath), request, response).serveResource()
    }

    @RequestMapping(value = ["/create"])
    fun create(request: HttpServletRequest?): String {
        //TODO 新建文件
        return ResponseDataUtils.error(ServiceErrorEnum.UNKNOWN)
    }

    @RequestMapping(value = ["/content"])
    fun content(request: HttpServletRequest, response: HttpServletResponse) {
        val sourcePath = RequestUtils.getSourcePath(request)
                ?: ServiceErrorEnum.MISSING_REQUIRED_PARAMETERS.throwout()
        val filePath = SKFileUtils.getContentPath(sourcePath)
        if (!Files.exists(filePath)) {
            ServiceErrorEnum.FILE_NOT_EXISTS.throwout()
        }
        response.writer.print(GetFileContent.byPath(filePath))
    }

    companion object {
        private val logger = LogManager.getLogger(FileController::class.java)
    }
}
