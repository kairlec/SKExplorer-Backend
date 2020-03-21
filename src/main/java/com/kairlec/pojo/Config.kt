package com.kairlec.pojo

import com.fasterxml.jackson.annotation.JsonIgnore
import com.kairlec.utils.LocalConfig.Companion.toJSON
import com.kairlec.utils.RSACoder

/**
 *@program: SKExplorer
 *@description: 配置文件
 *@author: Kairlec
 *@create: 2020-03-16 08:59
 */


data class Config(
        val adminUserArray: MutableList<User>,
        var privateKey: String,
        var publicKey: String,
        val systemConfig: SystemConfig
) {
    val json
        @JsonIgnore
        get() = run {
            adminUserArray.forEach { it.encode() }
            String.toJSON(this)
        }

    companion object {
        val Default: Config
            get() {
                val key = RSACoder.initKey()
                val publicKey = RSACoder.getPublicKey(key)!!
                val privateKey = RSACoder.getPrivateKey(key)!!
                return Config(mutableListOf(User.DefaultUserAdmin), privateKey, publicKey, SystemConfig.Default)
            }
    }
}
