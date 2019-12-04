package com.kairlec.dao;

import com.kairlec.pojo.DescriptionMap;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface DescriptionMapMapper {

    Integer init();

    List<DescriptionMap> getAll();

    DescriptionMap getDescriptionMap(@Param("path") String path);

    List<DescriptionMap> getSection(@Param("Paths") List<String> Paths);

    Integer insertDescriptionMap(DescriptionMap descriptionMap);

    Integer deleteDescriptionMap(@Param("path") String path);

    Integer updateDescriptionMap(DescriptionMap descriptionMap);
}


