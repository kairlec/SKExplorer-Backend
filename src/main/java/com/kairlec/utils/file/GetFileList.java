package com.kairlec.utils.file;


import com.kairlec.pojo.Json.FileInfo;
import com.kairlec.pojo.Json.FileList;
import com.kairlec.exception.ServiceError;
import com.kairlec.exception.SKException;
import com.kairlec.utils.LocalConfig;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class GetFileList {
    private static Logger logger = LogManager.getLogger(GetFileList.class);

    public static FileList byPath(Path path, String contentPath, List<String> excludeFile, List<String> excludeDir, List<String> excludeExt) throws SKException, IOException {

        FileList fileList = new FileList();
        try {
            if (Files.isSameFile(path, Paths.get(contentPath))) {
                fileList.setRoot(true);
                fileList.setLocalPath("/");
            } else {
                fileList.setRoot(false);
                fileList.setLocalPath(path.toString().substring(contentPath.length()).replaceAll("\\\\","/"));
            }
        } catch (IOException e) {
            e.printStackTrace();
            fileList.setRoot(false);
            fileList.setLocalPath(path.toString().substring(contentPath.length()).replaceAll("\\\\","/"));
        }
        if (!fileList.getLocalPath().startsWith("/")) {
            fileList.setLocalPath("/" + fileList.getLocalPath());
        }
        List<FileInfo> fileInfos = new ArrayList<>();
        List<String> filePaths = new ArrayList<>();

        DirectoryStream<Path> stream = Files.newDirectoryStream(path);
        try (stream) {
            for (Path subPath : stream) {
                FileInfo fileInfo;
                if (Files.isDirectory(subPath)) {
                    String subDirName = subPath.getFileName().toString();
                    if (excludeDir != null && excludeDir.contains(subDirName)) {
                        continue;
                    }
                    fileInfo = GetFileInfo.ByPath(contentPath, subPath);
                    fileInfos.add(fileInfo);
                    filePaths.add(fileInfo.getPath());
                } else {
                    String subFileName = subPath.getFileName().toString();
                    //判断是否为排除名单文件
                    if (excludeFile != null && excludeFile.contains(subFileName)) {
                        continue;
                    }
                    //判断是否为排除名单后缀
                    String ext = GetFileInfo.getExt(subPath);
                    if (excludeExt != null && excludeExt.contains(ext)) {
                        continue;
                    }
                    fileInfo = GetFileInfo.ByPath(contentPath, subPath);
                    fileInfos.add(fileInfo);
                    filePaths.add(fileInfo.getPath());
                }
            }
        }
        GetFileInfo.MatchDescription(fileInfos, filePaths);
        fileList.setItems(fileInfos);
        return fileList;
    }

}
