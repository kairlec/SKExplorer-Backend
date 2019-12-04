package com.kairlec.service;

import com.kairlec.pojo.DescriptionMap;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface DescriptionMapService {

    Integer init();

    List<DescriptionMap> getAll();

    DescriptionMap getDescriptionMap(String path);

    List<DescriptionMap> getSection(List<String> Paths);

    Integer insertDescriptionMap(DescriptionMap descriptionMap);

    Integer deleteDescriptionMap(String path);

    Integer updateDescriptionMap(DescriptionMap descriptionMap);
}
