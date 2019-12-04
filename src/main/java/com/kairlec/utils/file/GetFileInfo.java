package com.kairlec.utils.file;

import com.kairlec.pojo.DescriptionMap;
import com.kairlec.pojo.Json.FileInfo;
import com.kairlec.utils.LocalConfig;

import java.io.File;
import java.util.List;


public class GetFileInfo {

    public static void MatchDescription(List<FileInfo> fileInfos, List<String> Paths) {
        List<DescriptionMap> descriptionMapList = LocalConfig.getDescriptionMapService().getSection(Paths);
        if (descriptionMapList == null) {
            return;
        }
        for (DescriptionMap descriptionMap : descriptionMapList) {
            for (FileInfo fileInfo : fileInfos) {
                if (fileInfo.getPath().equals(descriptionMap.getPath())) {
                    fileInfo.setDescription(descriptionMap.getDescription());
                    break;
                }
            }
        }
    }

    public static FileInfo ByFile(File file) {
        FileInfo fileInfo = new FileInfo();
        fileInfo.setPath(file.getPath().replace(LocalConfig.getConfigBean().getContentdir(), "").replaceAll("\\\\", "/"));
        if (file.exists()) {
            fileInfo.setExist(true);
        } else {
            fileInfo.setExist(false);
            return fileInfo;
        }
        fileInfo.setName(file.getName());
        if (file.isDirectory()) {
            fileInfo.setType("folder");
        } else {
            if (fileInfo.getName().lastIndexOf('.') != -1) {
                fileInfo.setType(fileInfo.getName().substring(fileInfo.getName().lastIndexOf('.') + 1));
            } else {
                fileInfo.setType(null);
            }
        }
        fileInfo.setSize(file.length());
        fileInfo.setEditTime(file.lastModified());
        return fileInfo;
    }

    public static FileInfo ByPath(String Path) {
        return ByFile(new File(Path));
    }

}
