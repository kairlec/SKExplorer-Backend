package com.kairlec.service


import com.kairlec.intf.ResponseDataInterface
import com.kairlec.model.vo.ExtraInfo
import com.kairlec.model.vo.RelativePath
import com.kairlec.utils.VerifyAlgorithmEnum
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import java.net.URL
import java.nio.file.Path
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@Service
interface FileService {
    fun list(sourceDir: RelativePath): ResponseDataInterface
    fun upload(parts: List<MultipartFile>, sourceDir: RelativePath, isReplace: Boolean): ResponseDataInterface
    fun move(sourceDirOrFile: RelativePath, targetDir: RelativePath, isReplace: Boolean): ResponseDataInterface
    fun delete(sourceDirOrFile: RelativePath): ResponseDataInterface
    fun rename(sourceDirOrFile: RelativePath, targetName: String): ResponseDataInterface
    fun download(sourceFile: RelativePath, request: HttpServletRequest, response: HttpServletResponse)
    fun create(sourceDir: RelativePath, targetName: String, type: String, content: String?): ResponseDataInterface
    fun content(sourceFile: RelativePath): String
    fun extraInfo(sourceFile: RelativePath): ResponseDataInterface
    fun updateExtraInfo(sourceFile: RelativePath, extraInfo: ExtraInfo): ResponseDataInterface
    fun remoteDownload(sourceFile: RelativePath, url: URL): ResponseDataInterface
    fun verify(sourceFile: RelativePath, verifyAlgorithm: VerifyAlgorithmEnum): ResponseDataInterface
}
