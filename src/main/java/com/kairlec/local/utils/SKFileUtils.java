package com.kairlec.local.utils;

import com.kairlec.utils.LocalConfig;

public abstract class SKFileUtils {
    private SKFileUtils() {
        throw new AssertionError("工具类不允许实例化");
    }

    public static String getAbsolutePath(String relativePath) {
        return LocalConfig.getConfigBean().getContentdir() + relativePath;
    }

}
