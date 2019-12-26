package com.kairlec.utils.file;

import com.kairlec.pojo.DescriptionMap;
import com.kairlec.pojo.Json.FileInfo;
import com.kairlec.utils.LocalConfig;

import java.io.File;
import java.nio.file.Path;
import java.util.List;


public class GetFileInfo {
    public static String getExt(Path path) {
        String fileName = path.getFileName().toString();
        int pos = fileName.lastIndexOf('.');
        if (pos != -1) {
            return fileName.substring(pos + 1);
        } else {
            return null;
        }
    }

    public static String getExt(String fileName) {
        int pos = fileName.lastIndexOf('.');
        if (pos != -1) {
            return fileName.substring(pos + 1);
        } else {
            return null;
        }
    }

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

    public static FileInfo ByPath(String root, Path path) {
        return GetFileInfo.ByFile(root, path.toFile());
    }

    public static FileInfo ByFile(String root, File file) {
        FileInfo fileInfo = new FileInfo();
        fileInfo.setPath(file.getPath().replace(root, "").replaceAll("\\\\", "/"));
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
            fileInfo.setType(getExt(fileInfo.getName()));
            if (fileInfo.getType() != null && fileInfo.getType().equals("Redirect")) {
                fileInfo.setName(fileInfo.getName().substring(0, fileInfo.getName().lastIndexOf('.')));
                //System.out.println(fileInfo.getName());
                fileInfo.setType(getExt(fileInfo.getName()));
            }
        }
        fileInfo.setSize(file.length());
        fileInfo.setEditTime(file.lastModified());
        return fileInfo;
    }

}
