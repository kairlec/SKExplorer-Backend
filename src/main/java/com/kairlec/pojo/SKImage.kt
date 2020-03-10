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
    var bufferedImage: BufferedImage
    var contentType: String

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

    fun write(outputStream: OutputStream, Transparent: Boolean) {
        if (Transparent) {
            ImageIO.write(bufferedImage, "png", outputStream)
        } else {
            ImageIO.write(bufferedImage, "jpg", outputStream)
        }
    }

    fun toBase64(Transparent: Boolean? = null): String {
        var transparent = false
        if (Transparent == null) {
            transparent = contentType == "image/png"
        }
        val formatName: String = if (transparent) {
            "png"
        } else {
            "jpg"
        }
        var bytes: ByteArray = ByteArray(0)
        ByteArrayOutputStream().use {
            ImageIO.write(bufferedImage, formatName, it)
            bytes = it.toByteArray()
        }
        var base64String = Base64.getEncoder().encodeToString(bytes).trim();//转换成base64串
        base64String = base64String.replace("\n", "").replace("\r", "");
        return "data:${contentType};base64,$base64String"
    }

    fun read(file: File) {
        bufferedImage = ImageIO.read(file)
    }

    companion object {
        private val logger = LogManager.getLogger(SKImage::class.java)
        fun isImage(file: File): Boolean {
            if (!ImageIO.getImageReaders(ImageIO.createImageInputStream(file)).hasNext()) {
                return false
            }
            ImageIO.read(file) ?: return false
            return true
        }

        @Deprecated("此方法会读取流导致流内数据丢失")
        fun isImage(inputStream: InputStream): Boolean {
            ImageIO.read(inputStream) ?: return false
            return true
        }

        fun isImage(multipartFile: MultipartFile): Boolean {
            multipartFile.inputStream.use {
                ImageIO.read(it) ?: return false
                return true
            }
        }
    }
}