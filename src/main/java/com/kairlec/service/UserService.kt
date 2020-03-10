package com.kairlec.service

import com.kairlec.pojo.User
import org.springframework.stereotype.Service

@Service
interface UserService {
    fun init(): Int?
    fun all(): List<User>?
    fun getUser(username: String): User?
    fun insertUser(user: User): Int?
    fun deleteUser(username: String): Int?
    fun updateLoginInfo(user: User): Int?
    fun updatePassword(user: User): Int?
}
