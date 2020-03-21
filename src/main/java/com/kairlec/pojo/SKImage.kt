package com.kairlec.pojo

import org.apache.logging.log4j.LogManager
import org.springframework.web.multipart.MultipartFile
import java.awt.image.BufferedImage
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.InputStream
import java.io.OutputStream
import java.nio.file.Files
import java.nio.file.Path
import java.util.*
import javax.imageio.ImageIO

class SKImage {
    val bufferedImage: BufferedImage
    val contentType: String

    constructor(bufferedImage: BufferedImage, contentType: String) {
        this.bufferedImage = bufferedImage
        this.contentType = contentType
    }

    constructor(file: File) {
        bufferedImage = ImageIO.read(file)
        contentType = Files.probeContentType(file.toPath())
    }

    constructor(path: Path) {
        bufferedImage = ImageIO.read(path.toFile())
        contentType = Files.probeContentType(path)
    }

    fun write(outputStream: OutputStream) {
        ImageIO.write(bufferedImage, "jpg", outputStream)
    }

    fun write(outputStream: OutputStream, transparent: Boolean) {
        if (transparent) {
            ImageIO.write(bufferedImage, "png", outputStream)
        } else {
            ImageIO.write(bufferedImage, "jpg", outputStream)
        }
    }

    fun toBase64(transparent: Boolean = (contentType == "image/png")): String {
        val formatName: String = if (transparent) {
            "png"
        } else {
            "jpg"
        }
        ByteArrayOutputStream().use {
            ImageIO.write(bufferedImage, formatName, it)
            val base64String = Base64.getEncoder().encodeToString(it.toByteArray()).replace("[ \\n\\r]+".toRegex(), "")//转换成base64串
            return "data:${contentType};base64,$base64String"
        }
    }


    companion object {
        private val logger = LogManager.getLogger(SKImage::class.java)
        fun isImage(file: File) = run {
            ImageIO.read(file) ?: return false
            true
        }

        fun isImage(multipartFile: MultipartFile) =
                multipartFile.inputStream.use {
                    ImageIO.read(it) ?: return false
                    true
                }
    }
}