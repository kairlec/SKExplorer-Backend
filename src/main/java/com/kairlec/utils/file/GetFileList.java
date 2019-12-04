package com.kairlec.utils.file;


import com.kairlec.pojo.Json.FileInfo;
import com.kairlec.pojo.Json.FileList;
import com.kairlec.exception.ErrorCodeClass;
import com.kairlec.exception.SKException;
import com.kairlec.utils.LocalConfig;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class GetFileList {
    private static Logger logger = LogManager.getLogger(GetFileList.class);

    public static FileList byPath(String relativePath) throws SKException {
        FileList fileList = new FileList();
        if (relativePath.length() == 0 || relativePath .equals("/")) {
            fileList.setRoot(true);
        } else {
            fileList.setRoot(false);
        }
        File file = new File(LocalConfig.getConfigBean().getContentdir() + relativePath);
        if (!file.exists()) {
            System.out.println(file.getPath());
            logger.error(file.getPath() + " 不存在");
            throw new SKException(ErrorCodeClass.FILE_NOT_EXISTS);
        }
        if (!file.isDirectory()) {
            logger.error("请求文件列表" + file.getPath() + "不是文件夹");
            throw new SKException(ErrorCodeClass.NOT_DIR);
        }
        File[] files = file.listFiles();
        if (files == null) {
            return null;
        }
        List<FileInfo> fileInfos = new ArrayList<>();
        List<String> Paths = new ArrayList<>();
        for (File subFile : files) {
            FileInfo fileInfo;
            if (subFile.isFile()) {
                if (LocalConfig.getConfigBean().getExcludefile() != null && LocalConfig.getConfigBean().getExcludefile().contains(subFile.getName())) {
                    continue;
                }
                fileInfo = GetFileInfo.ByFile(subFile);
                fileInfos.add(fileInfo);
                Paths.add(fileInfo.getPath());
            }
            if (subFile.isDirectory()) {
                if (LocalConfig.getConfigBean().getExcludedir() != null && LocalConfig.getConfigBean().getExcludedir().contains(subFile.getName())) {
                    continue;
                }
                fileInfo = GetFileInfo.ByFile(subFile);
                fileInfos.add(fileInfo);
                Paths.add(fileInfo.getPath());
            }
        }
        GetFileInfo.MatchDescription(fileInfos, Paths);
        fileList.setItems(fileInfos);
        return fileList;
    }

}
