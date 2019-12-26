package com.kairlec.local.utils;

import com.kairlec.utils.LocalConfig;
import com.kairlec.utils.RSACoder;

import java.util.Base64;

public abstract class PasswordCoderUtils {
    private PasswordCoderUtils() {
        throw new AssertionError("工具类不允许实例化");
    }

    public static String fromRequest(String password) {
        try {
            return new String(RSACoder.decryptByPrivateKey(Base64.getDecoder().decode(password), LocalConfig.getPrivateKey()));
        } catch (Exception e) {
            e.printStackTrace();
            return password;
        }
    }


}
