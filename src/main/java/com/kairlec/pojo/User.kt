package com.kairlec.pojo

import com.kairlec.utils.VerifyAlgorithmEnum
import com.kairlec.utils.VerifyUtils
import com.kairlec.utils.randomString
import java.time.LocalDateTime;

data class User(
        var username: String,
        var password: String,
        var ip: String? = null,
        var lastLoginTime: LocalDateTime? = null,
        var lastSessionId: String? = null,
        var passwordEncoded: Boolean,
        var salt: String
) {
    fun encode() {
        if (!passwordEncoded) {
            password = VerifyUtils.getStringVerifyAsHex(username + password, VerifyAlgorithmEnum.SHA_1)
            passwordEncoded = true
        }
    }

    fun updatePassword(password: String, encode: Boolean = true) {
        if (encode) {
            this.password = VerifyUtils.getStringVerifyAsHex(username + password, VerifyAlgorithmEnum.SHA_1)
        } else {
            this.password = password
        }
        passwordEncoded = encode
        salt = ('a'..'z').randomString(3)
    }

    fun equalsPassword(password: String): Boolean {
        return VerifyUtils.verifyString(this.username + password, VerifyAlgorithmEnum.SHA_1, this.password)
    }

    companion object {
        val DefaultUserAdmin
            get() = User("admin", "admin", null, null, null, false, ('a'..'z').randomString(3))
    }
}
