package com.kairlec.config.startup

import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import java.io.File

/**
 *@program: SKExplorer
 *@description: 程序启动配置
 *@author: Kairlec
 *@create: 2020-03-08 18:07
 */

@Component
class StartupConfig {

    @Value("\${privatekey:#{null}}")
    fun setPrivateKey(privateKey: String) {
        Companion.privateKey = privateKey
    }

    @Value("\${publickey:#{null}}")
    fun setPublicKey(publicKey: String) {
        Companion.publicKey = publicKey
    }

    @Value("\${contentdir:#{null}}")
    fun setContentDir(contentDir: String) {
        if (contentDir.endsWith(File.separator)) {
            Companion.contentDir = contentDir
        } else {
            Companion.contentDir = contentDir + File.separator
        }
    }

    @Value("\${excludedir:}")
    fun setExcludeDir(excludeDir: Array<String>? = null) {
        if (excludeDir == null) {
            Companion.excludeDir = emptyArray()
        } else {
            Companion.excludeDir = excludeDir
        }
    }

    @Value("\${excludefile:}")
    fun setExcludeFile(excludeFile: Array<String>? = null) {
        if (excludeFile == null) {
            Companion.excludeFile = emptyArray()
        } else {
            Companion.excludeFile = excludeFile
        }
    }

    @Value("\${excludeext:}")
    fun setExcludeExt(excludeExt: Array<String>? = null) {
        if (excludeExt == null) {
            Companion.excludeExt = emptyArray()
        } else {
            Companion.excludeExt = excludeExt
        }
    }

    @Value("\${captchacount:0}")
    fun setCaptchaCount(captchaCount: Int) {
        Companion.captchaCount = captchaCount
    }


    companion object {
        lateinit var contentDir: String
        lateinit var excludeDir: Array<String>
        lateinit var excludeFile: Array<String>
        lateinit var excludeExt: Array<String>
        var captchaCount: Int = 4
        lateinit var privateKey: String
        lateinit var publicKey: String
    }

}