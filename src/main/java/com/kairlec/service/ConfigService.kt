package com.kairlec.service


import com.kairlec.model.bo.SystemConfig
import com.kairlec.model.bo.User
import org.springframework.stereotype.Service

@Service
interface ConfigService {
    fun getUser(username: String): User?

    fun addUser(user: User)

    fun updateUser(user: User)

    fun updateLoginInfo(user: User)

    fun getSystemConfig(): SystemConfig

    fun setSystemConfig(systemConfig: SystemConfig)

}