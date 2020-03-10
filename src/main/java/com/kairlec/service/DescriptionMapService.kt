package com.kairlec.service

import com.kairlec.pojo.DescriptionMap
import org.springframework.stereotype.Service

@Service
interface DescriptionMapService {
    fun init(): Int?
    fun all(): List<DescriptionMap>?
    fun getDescriptionMap(path: String): DescriptionMap?
    fun getSection(Paths: List<String>): List<DescriptionMap>?
    fun insertDescriptionMap(descriptionMap: DescriptionMap): Int?
    fun deleteDescriptionMap(path: String): Int?
    fun updateDescriptionMap(descriptionMap: DescriptionMap): Int?
}
