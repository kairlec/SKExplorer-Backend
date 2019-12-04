package com.kairlec.service;

import com.kairlec.pojo.MimeMap;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface MimeMapService {

    Integer init();

    List<MimeMap> getAll();

    MimeMap getMimeMap(String ext);

    List<MimeMap> getSection(List<String> exts);

    Integer insertMimeMap(MimeMap mimeMap);

    Integer deleteMimeMap(String ext);

    Integer updateMimeMap(MimeMap mimeMap);
}
