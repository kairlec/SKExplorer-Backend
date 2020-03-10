package com.kairlec.service.impl

import com.kairlec.dao.DescriptionMapMapper
import com.kairlec.pojo.DescriptionMap
import com.kairlec.service.DescriptionMapService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class DescriptionMapServiceImpl : DescriptionMapService {
    @Autowired
    private lateinit var descriptionMapMapper: DescriptionMapMapper

    override fun init(): Int? {
        return descriptionMapMapper.init()
    }

    override fun all(): List<DescriptionMap>? {
        return try {
            descriptionMapMapper.getAll()
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    override fun getSection(Paths: List<String>): List<DescriptionMap>? {
        return descriptionMapMapper.getSection(Paths)
    }

    override fun getDescriptionMap(path: String): DescriptionMap? {
        return try {
            descriptionMapMapper.getDescriptionMap(path)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    override fun insertDescriptionMap(descriptionMap: DescriptionMap): Int? {
        return try {
            descriptionMapMapper.insertDescriptionMap(descriptionMap)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    override fun deleteDescriptionMap(path: String): Int? {
        return try {
            descriptionMapMapper.deleteDescriptionMap(path)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    override fun updateDescriptionMap(descriptionMap: DescriptionMap): Int? {
        return try {
            descriptionMapMapper.updateDescriptionMap(descriptionMap)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
}
