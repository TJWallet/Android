package com.tianji.blockchain.util;

import android.annotation.SuppressLint;
import android.text.TextUtils;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class AESUtils {

    /***
     * AES 加密
     */
    private static final String AES = "AES";
    /***
     *  SHA1PRNG 强随机种子算法, 要区别4.2以上版本的调用方法
     */
    private static final String SHA1PRNG = "SHA1PRNG";
    private static final String IV = "ABCDEFGH01234567";

    /**
     * AES加密
     *
     * @param key       私钥
     * @param plaintext 明文
     * @return 返回String类型加密结果
     */
    public static String encrypt(String key, String plaintext) {
        if (TextUtils.isEmpty(plaintext)) {
            return null;
        }
        try {
            byte[] result = encrypt(key, plaintext.getBytes());
            if (result == null) {
                return null;
            }
            return parseByte2HexStr(result);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * AES加密
     *
     * @param key       私钥
     * @param plaintext 明文
     * @return 返回加密后的字节数组，MD5加密失败返回null
     * @throws Exception 加密失败异常
     */
    public static byte[] encrypt(String key, byte[] plaintext) throws Exception {
        String md5Result = MD5Utils.md5(key);
        if (md5Result == null) {
            return null;
        }
        SecretKey secretKey = new SecretKeySpec(md5Result.getBytes(), AES);
        @SuppressLint("GetInstance") Cipher cipher = Cipher.getInstance(AES);
        cipher.init(Cipher.ENCRYPT_MODE, secretKey, new IvParameterSpec(new byte[cipher.getBlockSize()]));
        return cipher.doFinal(plaintext);
    }

    /**
     * AES解密
     *
     * @param key        私钥
     * @param cipherText 密文
     * @return 返回String类型解密结果
     */
    public static String decrypt(String key, String cipherText) {
        if (TextUtils.isEmpty(cipherText)) {
            return null;
        }
        try {
            byte[] enc = parseHexStr2Byte(cipherText);
            byte[] result = decrypt(key, enc);
            if (result == null) {
                return null;
            }
            return new String(result);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * AES解密
     *
     * @param key        私钥
     * @param cipherText 密文
     * @return 返回解密后的字节数组，MD5加密失败返回null
     * @throws Exception 解密失败异常
     */
    public static byte[] decrypt(String key, byte[] cipherText) throws Exception {
        @SuppressLint("GetInstance") Cipher cipher = Cipher.getInstance(AES);
        String md5Result = MD5Utils.md5(key);
        if (md5Result == null) {
            return null;
        }
        SecretKey secretKey = new SecretKeySpec(md5Result.getBytes(), AES);
        cipher.init(Cipher.DECRYPT_MODE, secretKey, new IvParameterSpec(new byte[cipher.getBlockSize()]));
        return cipher.doFinal(cipherText);
    }

    /**
     * 生成随机数，可以当做动态的密钥
     * 加密和解密的密钥必须一致，不然将不能解密
     */
    public static String generateKey() {
        try {
            SecureRandom secureRandom = SecureRandom.getInstance(SHA1PRNG);
            byte[] key = new byte[20];
            secureRandom.nextBytes(key);
            return toHex(key);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 将二进制转换成16进制
     *
     * @param byteArray 字节数组
     * @return 返回String类型的16进制字符串
     */
    public static String toHex(byte[] byteArray) {
        if (byteArray == null) {
            return "";
        }
        StringBuffer result = new StringBuffer(2 * byteArray.length);
        for (byte b : byteArray) {
            appendHex(result, b);
        }
        return result.toString();
    }

    private static void appendHex(StringBuffer sb, byte b) {
        sb.append(IV.charAt((b >> 4) & 0x0f)).append(IV.charAt(b & 0x0f));
    }

    /**
     * 将二进制转换成16进制
     *
     * @param byteArray 字节数组
     * @return 返回String类型的16进制字符串
     */
    public static String parseByte2HexStr(byte[] byteArray) {
        StringBuilder sb = new StringBuilder();
        for (byte b : byteArray) {
            String hex = Integer.toHexString(b & 0xFF);
            if (hex.length() == 1) {
                hex = '0' + hex;
            }
            sb.append(hex.toUpperCase());
        }
        return sb.toString();
    }

    /**
     * 将16进制转换为二进制
     *
     * @param hexStr 需要转换的16进制字符串
     * @return 返回字节数组
     */
    public static byte[] parseHexStr2Byte(String hexStr) {
        if (hexStr.length() < 1) {
            return null;
        }
        byte[] byteArray = new byte[hexStr.length() / 2];
        for (int i = 0; i < hexStr.length() / 2; i++) {
            int high = Integer.parseInt(hexStr.substring(i * 2, i * 2 + 1), 16);
            int low = Integer.parseInt(hexStr.substring(i * 2 + 1, i * 2 + 2),
                    16);
            byteArray[i] = (byte) (high * 16 + low);
        }
        return byteArray;
    }
}
