package com.kairlec.service

import com.kairlec.pojo.MimeMap
import org.springframework.stereotype.Service

@Service
interface MimeMapService {
    fun init(): Int?
    fun all(): List<MimeMap>?
    fun getMimeMap(ext: String): MimeMap?
    fun getSection(exts: List<String>): List<MimeMap>?
    fun insertMimeMap(mimeMap: MimeMap): Int?
    fun deleteMimeMap(ext: String): Int?
    fun updateMimeMap(mimeMap: MimeMap): Int?
}
