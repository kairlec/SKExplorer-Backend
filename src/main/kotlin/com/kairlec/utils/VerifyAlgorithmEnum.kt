package com.kairlec.utils

/**
 *@program: FileSplit
 *@description: 校验算法枚举类
 *@author: Kairlec
 *@create: 2020-03-11 12:36
 */

enum class VerifyAlgorithmEnum(val code: Int, val algorithm: String, val byteArrayLength: Int) {
    SHA_512(6, org.apache.commons.codec.digest.MessageDigestAlgorithms.SHA_512, 128),
    SHA_384(5, org.apache.commons.codec.digest.MessageDigestAlgorithms.SHA_384, 96),
    SHA_256(4, org.apache.commons.codec.digest.MessageDigestAlgorithms.SHA_256, 64),
    SHA_224(3, org.apache.commons.codec.digest.MessageDigestAlgorithms.SHA_224, 56),
    SHA_1(2, org.apache.commons.codec.digest.MessageDigestAlgorithms.SHA_1, 40),
    MD5(1, org.apache.commons.codec.digest.MessageDigestAlgorithms.MD5, 32),
    NONE(-1, "", 0),
    ;


    companion object {
        fun getVerifyAlgorithmEnum(code: Int): VerifyAlgorithmEnum? {
            for (e in values()) {
                if (e.code == code) {
                    return e
                }
            }
            return null
        }

        fun getVerifyAlgorithmEnum(algorithmName: String): VerifyAlgorithmEnum? {
            for (e in values()) {
                if (e.name.replace("_", "").equals(algorithmName.replace("[\\_\\-\\ \\=]+".toRegex(), ""), true)) {
                    return e
                }
            }
            return null
        }
    }
}