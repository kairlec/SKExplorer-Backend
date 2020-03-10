package com.kairlec.service.impl

import com.kairlec.dao.MimeMapMapper
import com.kairlec.pojo.MimeMap
import com.kairlec.service.MimeMapService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class MimeMapServiceImpl : MimeMapService {
    @Autowired
    private lateinit var mimeMapMapper: MimeMapMapper

    override fun init(): Int? {
        return mimeMapMapper.init()
    }

    override fun getMimeMap(ext: String): MimeMap? {
        return mimeMapMapper.getMimeMap(ext)
    }

    override fun all(): List<MimeMap>? {
        return try {
            mimeMapMapper.getAll()
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    override fun getSection(exts: List<String>): List<MimeMap>? {
        return mimeMapMapper.getSection(exts)
    }

    override fun insertMimeMap(mimeMap: MimeMap): Int? {
        return try {
            mimeMapMapper.insertMimeMap(mimeMap)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    override fun deleteMimeMap(ext: String): Int? {
        return try {
            mimeMapMapper.deleteMimeMap(ext)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    override fun updateMimeMap(mimeMap: MimeMap): Int? {
        return try {
            mimeMapMapper.updateMimeMap(mimeMap)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
}
