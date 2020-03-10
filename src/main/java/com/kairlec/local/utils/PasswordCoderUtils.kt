package com.kairlec.local.utils

import com.kairlec.config.startup.StartupConfig
import com.kairlec.utils.LocalConfig
import com.kairlec.utils.RSACoder
import java.util.*

object PasswordCoderUtils {
    fun fromRequest(password: String): String {
        return try {
            String(RSACoder.decryptByPrivateKey(Base64.getDecoder().decode(password), StartupConfig.privateKey))
        } catch (e: Exception) {
            e.printStackTrace()
            password
        }
    }
}
