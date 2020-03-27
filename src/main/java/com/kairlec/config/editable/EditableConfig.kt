package com.kairlec.config.editable

/**
 * @author: Kairlec
 * @version: 1.0
 * @description: 在程序中可更改动态加载的配置项目
 * @suppress
 */

import com.kairlec.dao.ConfigDao
import com.kairlec.constant.ServiceErrorEnum
import com.kairlec.model.bo.User
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger
import org.springframework.stereotype.Component

@Component
object EditableConfig {
    private val logger: Logger = LogManager.getLogger(EditableConfig::class.java)
    val config
        get() = ConfigDao.Instance

    fun getUser(username: String): User? {
        return config.adminUserArray.find { it.username == username }
    }

    fun addUser(user: User) {
        config.adminUserArray.find { it.username == user.username }?.let { ServiceErrorEnum.USERNAME_NOT_EXISTS.throwout() }
        config.adminUserArray.add(user)
    }

    fun save() {
        ConfigDao.readyEdit()
        ConfigDao.saveConfig()
    }


}