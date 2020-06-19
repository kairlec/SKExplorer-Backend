package com.kairlec.model.bo

import com.fasterxml.jackson.databind.annotation.JsonDeserialize
import com.fasterxml.jackson.databind.annotation.JsonSerialize
import com.kairlec.local.jackson.LocalDateTimeDeserializer
import com.kairlec.local.jackson.LocalDateTimeSerializer
import com.kairlec.utils.VerifyAlgorithmEnum
import com.kairlec.utils.VerifyUtils
import com.kairlec.utils.randomString
import java.time.LocalDateTime;

data class User(
        var username: String,
        var password: String,
        var ip: String? = null,
        @JsonSerialize(using = LocalDateTimeSerializer::class)
        @JsonDeserialize(using = LocalDateTimeDeserializer::class)
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

    fun updateTo(user: User) {
        this.updatePassword(user.password, !user.passwordEncoded)
    }

    fun updateLoginInfo(user: User) {
        this.ip = user.ip
        this.lastSessionId = user.lastSessionId
        this.lastLoginTime = LocalDateTime.now()
    }

    companion object {
        val DefaultUserAdmin
            get() = User("admin", "admin", null, null, null, false, ('a'..'z').randomString(3))
    }
}
