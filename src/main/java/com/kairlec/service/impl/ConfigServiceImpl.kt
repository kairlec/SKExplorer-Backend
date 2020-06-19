package com.kairlec.service.impl

import com.kairlec.constant.ServiceErrorEnum
import com.kairlec.dao.ConfigDao
import com.kairlec.model.bo.SystemConfig
import com.kairlec.model.bo.User
import com.kairlec.service.ConfigService
import org.springframework.stereotype.Service

@Service
class ConfigServiceImpl : ConfigService {
    private val config
        get() = ConfigDao.Instance

    private fun save() {
        ConfigDao.readyEdit()
        ConfigDao.saveConfig()
    }

    override fun getUser(username: String): User? {
        return config.adminUserArray.find { it.username == username }
    }

    fun getUser(verify: (User) -> Boolean): User? {
        return config.adminUserArray.find(verify)
    }

    override fun addUser(user: User) {
        config.adminUserArray.find { it.username == user.username }?.let { ServiceErrorEnum.USERNAME_ALREADY_EXISTS.throwout() }
        config.adminUserArray.add(user)
        save()
    }

    override fun updateUser(user: User) {
        val existUser = config.adminUserArray.find { it.username == user.username }
                ?: ServiceErrorEnum.USERNAME_NOT_EXISTS.throwout()
        existUser.updateTo(user)
        save()
    }

    override fun updateLoginInfo(user: User) {
        val existUser = config.adminUserArray.find { it.username == user.username }
                ?: ServiceErrorEnum.USERNAME_NOT_EXISTS.throwout()
        existUser.updateLoginInfo(user)
        save()
    }

    override fun getSystemConfig(): SystemConfig {
        return config.systemConfig
    }

    override fun setSystemConfig(systemConfig: SystemConfig) {
        config.systemConfig.updateTo(systemConfig)
        save()
    }

}