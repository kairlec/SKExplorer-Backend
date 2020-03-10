package com.kairlec.pojo

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.kairlec.annotation.NoArg
import org.springframework.util.DigestUtils;

import java.time.LocalDateTime;

@NoArg
data class User(
        var username: String,
        @JSONField(serialize = false)
        var password: String,
        @JSONField(name = "IP")
        var IP: String? = null,
        var lastLoginTime: LocalDateTime? = null,
        var email: String,
        var lastSessionId: String? = null
) {

    fun updatePassword(password: String) {
        this.password = DigestUtils.md5DigestAsHex(password.toByteArray())
    }

    fun equalsPassword(password: String): Boolean {
        return this.password == DigestUtils.md5DigestAsHex(password.toByteArray())
    }

    override fun toString(): String {
        return JSON.toJSONString(this, SerializerFeature.WriteMapNullValue)
    }
}
