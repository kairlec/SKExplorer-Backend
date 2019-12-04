package com.kairlec.service.impl;

import com.kairlec.dao.DescriptionMapMapper;
import com.kairlec.pojo.DescriptionMap;
import com.kairlec.service.DescriptionMapService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DescriptionMapServiceImpl implements DescriptionMapService {
    @Autowired
    private DescriptionMapMapper descriptionMapMapper;

    @Override
    public Integer init() {
        return descriptionMapMapper.init();
    }

    @Override
    public List<DescriptionMap> getAll() {
        try {
            return descriptionMapMapper.getAll();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public List<DescriptionMap> getSection(List<String> Paths) {
        return descriptionMapMapper.getSection(Paths);
    }

    @Override
    public DescriptionMap getDescriptionMap(String ext) {
        try {
            return descriptionMapMapper.getDescriptionMap(ext);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public Integer insertDescriptionMap(DescriptionMap descriptionMap) {
        try {
            return descriptionMapMapper.insertDescriptionMap(descriptionMap);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public Integer deleteDescriptionMap(String path) {
        try {
            return descriptionMapMapper.deleteDescriptionMap(path);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public Integer updateDescriptionMap(DescriptionMap descriptionMap) {
        try {
            return descriptionMapMapper.updateDescriptionMap(descriptionMap);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
