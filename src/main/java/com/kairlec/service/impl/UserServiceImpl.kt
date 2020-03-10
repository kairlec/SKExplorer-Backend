package com.kairlec.service.impl

import com.kairlec.dao.UserMapper
import com.kairlec.pojo.User
import com.kairlec.service.UserService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class UserServiceImpl : UserService {
    @Autowired
    private lateinit var userMapper: UserMapper

    override fun init(): Int? {
        return userMapper.init()
    }

    override fun all(): List<User>? {
        return try {
            userMapper.getAll()
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    override fun getUser(username: String): User? {
        return try {
            userMapper.getUser(username)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    override fun insertUser(user: User): Int? {
        return try {
            userMapper.insertUser(user)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    override fun deleteUser(username: String): Int? {
        return try {
            userMapper.deleteUser(username)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    override fun updateLoginInfo(user: User): Int? {
        return try {
            userMapper.updateLoginInfo(user)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    override fun updatePassword(user: User): Int? {
        return try {
            userMapper.updatePassword(user)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
}
