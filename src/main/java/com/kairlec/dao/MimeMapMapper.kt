package com.kairlec.dao


import com.kairlec.pojo.MimeMap
import org.apache.ibatis.annotations.Mapper
import org.apache.ibatis.annotations.Param

@Mapper
interface MimeMapMapper {
    fun init(): Int?
    fun getAll(): List<MimeMap>?
    fun getMimeMap(@Param("ext") ext: String): MimeMap?
    fun getSection(@Param("exts") exts: List<String>): List<MimeMap>?
    fun insertMimeMap(mimeMap: MimeMap): Int?
    fun deleteMimeMap(@Param("ext") ext: String): Int?
    fun updateMimeMap(mimeMap: MimeMap): Int?
}
