package com.kairlec.utils;

import java.util.Base64;

public class PasswordCoder {
    public static String fromDatabase(String password) {
        try {
            return new String(RSACoder.decryptByPublicKey(Base64.getDecoder().decode(password), LocalConfig.getPublicKey()));
        } catch (Exception e) {
            e.printStackTrace();
            return password;
        }
    }

    public static String toDatabase(String password) {
        try {
            return Base64.getEncoder().encodeToString(RSACoder.encryptByPrivateKey(password.getBytes(), LocalConfig.getPrivateKey()));
        } catch (Exception e) {
            e.printStackTrace();
            return password;
        }
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
