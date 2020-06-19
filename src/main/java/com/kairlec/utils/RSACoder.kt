package com.kairlec.utils

import java.security.Key
import java.security.KeyFactory
import java.security.KeyPairGenerator
import java.security.Signature
import java.security.spec.PKCS8EncodedKeySpec
import java.security.spec.X509EncodedKeySpec
import java.util.*
import javax.crypto.Cipher

/**
 *@program: Backend
 *@description: RSA非对称加密解密的工具
 *@author: Kairlec
 *@create: 2020-02-22 20:28
 */

object RSACoder {
    const val KEY_ALGORITHM = "RSA"
    const val SIGNATURE_ALGORITHM = "MD5withRSA"
    private const val PUBLIC_KEY = "RSAPublicKey"
    private const val PRIVATE_KEY = "RSAPrivateKey"

    fun decryptBASE64(key: String): ByteArray {
        return Base64.getDecoder().decode(key)
    }

    fun encryptBASE64(bytes: ByteArray): String {
        return Base64.getEncoder().encodeToString(bytes)
    }

    /**
     * 用私钥对信息生成数字签名
     *
     * @param data       加密数据
     * @param privateKey 私钥
     * @return kj
     */
    fun sign(data: ByteArray, privateKey: String): String { // 解密由base64编码的私钥
        val keyBytes = decryptBASE64(privateKey)
        // 构造PKCS8EncodedKeySpec对象
        val pkcs8KeySpec = PKCS8EncodedKeySpec(keyBytes)
        // KEY_ALGORITHM 指定的加密算法
        val keyFactory = KeyFactory.getInstance(KEY_ALGORITHM)
        // 取私钥匙对象
        val priKey = keyFactory.generatePrivate(pkcs8KeySpec)
        // 用私钥对信息生成数字签名
        val signature = Signature.getInstance(SIGNATURE_ALGORITHM)
        signature.initSign(priKey)
        signature.update(data)
        return encryptBASE64(signature.sign())
    }

    /**
     * 校验数字签名
     *
     * @param data      加密数据
     * @param publicKey 公钥
     * @param sign      数字签名
     * @return 校验成功返回true 失败返回false
     */
    fun verify(data: ByteArray, publicKey: String, sign: String): Boolean { // 解密由base64编码的公钥
        val keyBytes = decryptBASE64(publicKey)
        // 构造X509EncodedKeySpec对象
        val keySpec = X509EncodedKeySpec(keyBytes)
        // KEY_ALGORITHM 指定的加密算法
        val keyFactory = KeyFactory.getInstance(KEY_ALGORITHM)
        // 取公钥匙对象
        val pubKey = keyFactory.generatePublic(keySpec)
        val signature = Signature.getInstance(SIGNATURE_ALGORITHM)
        signature.initVerify(pubKey)
        signature.update(data)
        // 验证签名是否正常
        return signature.verify(decryptBASE64(sign))
    }

    fun decryptByPrivateKey(data: ByteArray, key: String): ByteArray { // 对密钥解密
        val keyBytes = decryptBASE64(key)
        // 取得私钥
        val pkcs8KeySpec = PKCS8EncodedKeySpec(keyBytes)
        val keyFactory = KeyFactory.getInstance(KEY_ALGORITHM)
        val privateKey: Key = keyFactory.generatePrivate(pkcs8KeySpec)
        // 对数据解密
        val cipher = Cipher.getInstance(keyFactory.algorithm)
        cipher.init(Cipher.DECRYPT_MODE, privateKey)
        return cipher.doFinal(data)
    }

    /**
     * 解密<br></br>
     * 用私钥解密
     *
     * @param data
     * @param key
     * @return
     */
    fun decryptByPrivateKey(data: String, key: String): ByteArray {
        return decryptByPrivateKey(decryptBASE64(data), key)
    }

    /**
     * 解密<br></br>
     * 用私钥解密
     *
     * @param data
     * @param key
     * @return
     */
    fun decryptByPrivateKeyToString(data: String, key: String): String {
        return String(decryptByPrivateKey(data, key))
    }

    /**
     * 解密<br></br>
     * 用公钥解密
     *
     * @param data
     * @param key
     * @return
     */
    fun decryptByPublicKey(data: ByteArray, key: String): ByteArray { // 对密钥解密
        val keyBytes = decryptBASE64(key)
        // 取得公钥
        val x509KeySpec = X509EncodedKeySpec(keyBytes)
        val keyFactory = KeyFactory.getInstance(KEY_ALGORITHM)
        val publicKey: Key = keyFactory.generatePublic(x509KeySpec)
        // 对数据解密
        val cipher = Cipher.getInstance(keyFactory.algorithm)
        cipher.init(Cipher.DECRYPT_MODE, publicKey)
        return cipher.doFinal(data)
    }

    fun decryptByPublicKey(data: String, key: String): ByteArray {
        return decryptByPublicKey(decryptBASE64(data), key)
    }

    fun decryptByPublicKeyToString(data: String, key: String): String {
        return String(decryptByPublicKey(data, key))
    }

    /**
     * 加密<br></br>
     * 用公钥加密
     *
     * @param data
     * @param key
     * @return
     */
    fun encryptByPublicKey(data: String, key: String): ByteArray { // 对公钥解密
        val keyBytes = decryptBASE64(key)
        // 取得公钥
        val x509KeySpec = X509EncodedKeySpec(keyBytes)
        val keyFactory = KeyFactory.getInstance(KEY_ALGORITHM)
        val publicKey: Key = keyFactory.generatePublic(x509KeySpec)
        // 对数据加密
        val cipher = Cipher.getInstance(keyFactory.algorithm)
        cipher.init(Cipher.ENCRYPT_MODE, publicKey)
        return cipher.doFinal(data.toByteArray())
    }

    /**
     * 加密<br></br>
     * 用公钥加密
     *
     * @param data
     * @param key
     * @return
     */
    fun encryptByPublicKeyToString(data: String, key: String): String {
        return encryptBASE64(encryptByPublicKey(data, key))
    }

    /**
     * 加密<br></br>
     * 用私钥加密
     *
     * @param data
     * @param key
     * @return
     */
    fun encryptByPrivateKey(data: ByteArray?, key: String): ByteArray { // 对密钥解密
        val keyBytes = decryptBASE64(key)
        // 取得私钥
        val pkcs8KeySpec = PKCS8EncodedKeySpec(keyBytes)
        val keyFactory = KeyFactory.getInstance(KEY_ALGORITHM)
        val privateKey: Key = keyFactory.generatePrivate(pkcs8KeySpec)
        // 对数据加密
        val cipher = Cipher.getInstance(keyFactory.algorithm)
        cipher.init(Cipher.ENCRYPT_MODE, privateKey)
        return cipher.doFinal(data)
    }

    /**
     * 加密<br></br>
     * 用私钥加密
     *
     * @param data
     * @param key
     * @return
     */
    fun encryptByPrivateKeyToString(data: ByteArray, key: String): String {
        return encryptBASE64(encryptByPrivateKey(data, key))
    }

    /**
     * 加密<br></br>
     * 用私钥加密
     *
     * @param data
     * @param key
     * @return
     */
    fun encryptByPrivateKeyToString(data: String, key: String): String {
        return encryptBASE64(encryptByPrivateKey(data.toByteArray(), key))
    }

    /**
     * 取得私钥
     *
     * @param keyMap
     * @return
     */
    fun getPrivateKey(keyMap: Map<String, Key>): String? {
        val encodedKey = keyMap[PRIVATE_KEY]?.encoded ?: return null
        return encryptBASE64(encodedKey)
    }

    /**
     * 取得公钥
     *
     * @param keyMap
     * @return
     */
    fun getPublicKey(keyMap: Map<String, Key>): String? {
        val encodedKey = keyMap[PUBLIC_KEY]?.encoded ?: return null
        return encryptBASE64(encodedKey)
    }

    /**
     * 初始化密钥
     * @return
     */
    fun initKey(): Map<String, Key> {
        val keyPairGen = KeyPairGenerator
                .getInstance(KEY_ALGORITHM)
        keyPairGen.initialize(1024)
        val keyPair = keyPairGen.generateKeyPair()
        val keyMap: MutableMap<String, Key> = HashMap(2)
        keyMap[PUBLIC_KEY] = keyPair.public // 公钥
        keyMap[PRIVATE_KEY] = keyPair.private // 私钥
        return keyMap
    }
}