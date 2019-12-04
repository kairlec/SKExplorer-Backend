package com.kairlec.service.impl;

import com.kairlec.dao.MimeMapMapper;
import com.kairlec.pojo.MimeMap;
import com.kairlec.service.MimeMapService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MimeMapServiceImpl implements MimeMapService {
    @Autowired
    private MimeMapMapper mimeMapMapper;

    @Override
    public Integer init() {
        return mimeMapMapper.init();
    }

    @Override
    public MimeMap getMimeMap(String ext) {
        return mimeMapMapper.getMimeMap(ext);
    }

    @Override
    public List<MimeMap> getAll() {
        try {
            return mimeMapMapper.getAll();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public List<MimeMap> getSection(List<String> exts) {
        return mimeMapMapper.getSection(exts);
    }

    @Override
    public Integer insertMimeMap(MimeMap mimeMap) {
        try {
            return mimeMapMapper.insertMimeMap(mimeMap);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public Integer deleteMimeMap(String ext) {
        try {
            return mimeMapMapper.deleteMimeMap(ext);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public Integer updateMimeMap(MimeMap mimeMap) {
        try {
            return mimeMapMapper.updateMimeMap(mimeMap);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

}
