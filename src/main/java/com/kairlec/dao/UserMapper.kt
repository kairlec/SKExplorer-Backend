package com.kairlec.dao;

import com.kairlec.pojo.User
import org.apache.ibatis.annotations.Mapper
import org.apache.ibatis.annotations.Param

@Mapper
interface UserMapper {
    fun init(): Int?
    fun getAll(): List<User>?
    fun getUser(@Param("username") username: String): User?
    fun insertUser(user: User): Int?
    fun deleteUser(@Param("username") username: String): Int?
    fun updateLoginInfo(user: User): Int?
    fun updatePassword(user: User): Int?
}
