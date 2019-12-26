package com.kairlec.local.utils;

import com.kairlec.utils.LocalConfig;
import com.kairlec.utils.file.GetFileContent;

import java.nio.file.Path;
import java.nio.file.Paths;

public abstract class SKFileUtils {
    private SKFileUtils() {
        throw new AssertionError("工具类不允许实例化");
    }

    public static Path getContentPath(String relativePath) {
        return Paths.get(LocalConfig.getConfigBean().getContentdir() + relativePath);
    }

    public static Path getLogPath(String logPath, String relativePath) {
        return Paths.get(logPath + relativePath);
    }

    public static String getExt(Path path) {
        String fileName = path.getFileName().toString();
        int pos = fileName.lastIndexOf('.');
        if (pos != -1) {
            return fileName.substring(pos + 1);
        } else {
            return null;
        }
    }

}
