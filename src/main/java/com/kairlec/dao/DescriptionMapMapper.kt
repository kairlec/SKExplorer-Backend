package com.kairlec.dao

import com.kairlec.pojo.DescriptionMap
import org.apache.ibatis.annotations.Mapper
import org.apache.ibatis.annotations.Param

@Mapper
interface DescriptionMapMapper {
    fun init(): Int?
    fun getAll(): List<DescriptionMap>?

    fun getDescriptionMap(@Param("path") path: String): DescriptionMap?

    /**
     * 获取部分描述
     * @return
     * */
    fun getSection(@Param("Paths") Paths: List<String>): List<DescriptionMap>?

    fun insertDescriptionMap(descriptionMap: DescriptionMap): Int?

    fun deleteDescriptionMap(@Param("path") path: String): Int?

    fun updateDescriptionMap(descriptionMap: DescriptionMap): Int?
}
