package com.kairlec.dao;


import com.kairlec.pojo.MimeMap;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface MimeMapMapper {

    Integer init();

    List<MimeMap> getAll();

    MimeMap getMimeMap(@Param("ext") String ext);

    List<MimeMap> getSection(@Param("exts") List<String> exts);

    Integer insertMimeMap(MimeMap mimeMap);

    Integer deleteMimeMap(@Param("ext") String ext);

    Integer updateMimeMap(MimeMap mimeMap);

}
