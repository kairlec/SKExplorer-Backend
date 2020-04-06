package com.kairlec.controller

import com.kairlec.annotation.JsonRequestMapping
import com.kairlec.constant.ServiceErrorEnum
import com.kairlec.intf.ResponseDataInterface
import com.kairlec.model.vo.ExtraInfo
import com.kairlec.model.vo.RelativePath
import com.kairlec.service.impl.FileServiceImpl
import com.kairlec.utils.VerifyAlgorithmEnum
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.multipart.MultipartFile
import java.net.MalformedURLException
import java.net.URL
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse


/**
 *@program: SKExplorer
 *@description: 文件API
 *@author: Kairlec
 *@create: 2020-03-08 18:11
 */
@JsonRequestMapping(value = ["/file"], method = [RequestMethod.POST])
@RestController
class FileController {

    @Autowired
    private lateinit var fileServiceImpl: FileServiceImpl

    @RequestMapping(value = ["/list"])
    fun list(@RequestParam(name = "sourceDir", required = false, defaultValue = "/") sourceDir: RelativePath): ResponseDataInterface {
        return fileServiceImpl.list(sourceDir)
    }

    @RequestMapping(value = ["/upload"])
    fun upload(@RequestParam(name = "file") parts: List<MultipartFile>,
               @RequestParam(name = "sourceDir") sourceDir: RelativePath,
               @RequestParam(name = "replace", required = false, defaultValue = "false") isReplace: Boolean
    ): ResponseDataInterface {
        return fileServiceImpl.upload(parts, sourceDir, isReplace)
    }

    @RequestMapping(value = ["/move"])
    fun move(@RequestParam(name = "sourceDirOrFile") sourceDirOrFile: RelativePath,
             @RequestParam(name = "targetDir") targetDir: RelativePath,
             @RequestParam(name = "replace", required = false, defaultValue = "false") isReplace: Boolean
    ): ResponseDataInterface {
        return fileServiceImpl.move(sourceDirOrFile, targetDir, isReplace)
    }

    @RequestMapping(value = ["/delete"])
    fun delete(@RequestParam(name = "sourceDirOrFile") sourceDirOrFile: RelativePath): ResponseDataInterface {
        return fileServiceImpl.delete(sourceDirOrFile)
    }

    @RequestMapping(value = ["/rename"])
    fun rename(@RequestParam(name = "sourceDirOrFile") sourceDirOrFile: RelativePath,
               @RequestParam(name = "targetName") targetName: String
    ): ResponseDataInterface {
        return fileServiceImpl.rename(sourceDirOrFile, targetName)
    }

    @RequestMapping(value = ["/download"], method = [RequestMethod.GET])
    fun download(@RequestParam(name = "sourceFile") sourceFile: RelativePath,
                 request: HttpServletRequest,
                 response: HttpServletResponse) {
        fileServiceImpl.download(sourceFile, request, response)
    }

    @RequestMapping(value = ["/create"])
    fun create(@RequestParam(name = "sourceDir") sourceDir: RelativePath,
               @RequestParam(name = "targetName") targetName: String,
               @RequestParam(name = "type") type: String,
               @RequestParam(name = "content", required = false) content: String?): ResponseDataInterface {
        if (type != "file" || type != "folder") {
            ServiceErrorEnum.UNKNOWN_REQUEST.throwout()
        }
        return fileServiceImpl.create(sourceDir, targetName, type, content)
    }

    @RequestMapping(value = ["/content"], produces = ["text/plain"])
    fun content(@RequestParam(name = "sourceFile") sourceFile: RelativePath): String {
        return fileServiceImpl.content(sourceFile)
    }

    @RequestMapping(value = ["/extraInfo/get"])
    fun extraInfo(@RequestParam(name = "sourceFile") sourceFile: RelativePath): ResponseDataInterface {
        return fileServiceImpl.extraInfo(sourceFile)
    }

    @RequestMapping(value = ["/extraInfo/update"])
    fun updateExtraInfo(@RequestParam(name = "sourceFile") sourceFile: RelativePath,
                        @RequestParam(name = "extraInfo") extraInfo: ExtraInfo
    ): ResponseDataInterface {
        return fileServiceImpl.updateExtraInfo(sourceFile, extraInfo)
    }

    @RequestMapping(value = ["/remoteDownload"])
    fun remoteDownload(@RequestParam(name = "sourceFile") sourceFile: RelativePath,
                       @RequestParam(name = "url") urlString: String
    ): ResponseDataInterface {
        val url: URL
        try {
            url = URL(urlString)
        } catch (e: MalformedURLException) {
            ServiceErrorEnum.WRONG_URL.data(e.message).throwout()
        }
        return fileServiceImpl.remoteDownload(sourceFile, url)
    }

    @RequestMapping(value = ["/verify"])
    fun verify(@RequestParam(name = "sourceFile") sourceFile: RelativePath,
               @RequestParam(name = "verifyAlgorithmCode", required = false) verifyAlgorithmCode: Int?,
               @RequestParam(name = "verifyAlgorithm", required = false) verifyAlgorithmString: String?): ResponseDataInterface {
        var verifyAlgorithm: VerifyAlgorithmEnum? = null
        verifyAlgorithmCode?.let {
            verifyAlgorithm = VerifyAlgorithmEnum.getVerifyAlgorithmEnum(it)
        }
        verifyAlgorithmString?.let { algorithm ->
            VerifyAlgorithmEnum.getVerifyAlgorithmEnum(algorithm)?.let {
                verifyAlgorithm = it
            }
        }
        verifyAlgorithm ?: ServiceErrorEnum.MISSING_REQUIRED_PARAMETERS.throwout()
        return fileServiceImpl.verify(sourceFile, verifyAlgorithm!!)
    }

}
