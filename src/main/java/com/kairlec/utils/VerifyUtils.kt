package com.kairlec.utils

import org.apache.commons.codec.digest.DigestUtils
import java.io.File

object VerifyUtils {
    fun getFileVerifyAsByteArray(file: File, algorithm: VerifyAlgorithmEnum): ByteArray {
        if (algorithm.code == -1) {
            return ByteArray(0)
        }
        return DigestUtils(algorithm.algorithm).digest(file)
    }

    fun getFileVerifyAsHex(file: File, algorithm: VerifyAlgorithmEnum): String {
        if (algorithm.code == -1) {
            return ""
        }
        return DigestUtils(algorithm.algorithm).digestAsHex(file)
    }

    fun getStringVerifyAsByteArray(content: String, algorithm: VerifyAlgorithmEnum): ByteArray {
        if (algorithm.code == -1) {
            return ByteArray(0)
        }
        return DigestUtils(algorithm.algorithm).digest(content)
    }

    fun getStringVerifyAsHex(content: String, algorithm: VerifyAlgorithmEnum): String {
        if (algorithm.code == -1) {
            return ""
        }
        return DigestUtils(algorithm.algorithm).digestAsHex(content)
    }

    fun verifyString(content: String, algorithm: VerifyAlgorithmEnum, verifyCode: ByteArray): Boolean {
        if (algorithm.code == -1) {
            return true
        }
        return verifyCode.contentEquals(DigestUtils(algorithm.algorithm).digest(content))
    }

    fun verifyString(content: String, algorithm: VerifyAlgorithmEnum, verifyCode: String): Boolean {
        if (algorithm.code == -1) {
            return true
        }
        return verifyCode == DigestUtils(algorithm.algorithm).digestAsHex(content)
    }

    fun verifyFile(file: File, algorithm: VerifyAlgorithmEnum, verifyCode: ByteArray): Boolean {
        if (algorithm.code == -1) {
            return true
        }
        return verifyCode.contentEquals(DigestUtils(algorithm.algorithm).digest(file))
    }

    fun verifyFile(file: File, algorithm: VerifyAlgorithmEnum, verifyCode: String): Boolean {
        if (algorithm.code == -1) {
            return true
        }
        return verifyCode == DigestUtils(algorithm.algorithm).digestAsHex(file)
    }

}