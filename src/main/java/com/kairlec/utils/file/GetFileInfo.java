package com.kairlec.utils.file;

import com.kairlec.pojo.DescriptionMap;
import com.kairlec.pojo.Json.FileInfo;
import com.kairlec.utils.LocalConfig;

import java.io.File;
import java.util.List;


public class GetFileInfo {

    private static String getType(String filename) {
        if (filename.lastIndexOf('.') != -1) {
            return filename.substring(filename.lastIndexOf('.') + 1);
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
            fileInfo.setType(getType(fileInfo.getName()));
            if (fileInfo.getType() != null && fileInfo.getType().equals("Redirect")) {
                fileInfo.setName(fileInfo.getName().substring(0, fileInfo.getName().lastIndexOf('.')));
                //System.out.println(fileInfo.getName());
                fileInfo.setType(getType(fileInfo.getName()));
                fileInfo.setRedirect(true);
            } else {
                fileInfo.setRedirect(false);
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
