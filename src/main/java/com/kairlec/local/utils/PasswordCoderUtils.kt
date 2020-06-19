package com.kairlec.local.utils

import com.kairlec.dao.ConfigDao
import com.kairlec.utils.RSACoder
import java.util.*

object PasswordCoderUtils {
    fun fromRequest(password: String): String {
        return try {
            String(RSACoder.decryptByPrivateKey(Base64.getDecoder().decode(password), ConfigDao.Instance.privateKey))
        } catch (e: Exception) {
            e.printStackTrace()
            password
        }
    }
}
