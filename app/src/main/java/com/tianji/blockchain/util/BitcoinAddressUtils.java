package com.tianji.blockchain.util;

import org.bitcoinj.core.Base58;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class BitcoinAddressUtils {

    /**
     * 将16进制私钥转成WIF格式或WIF-compressed格式
     *
     * @param hexPrivKey 需要转换的比特币16进制私钥
     * @param flag       布尔值，是否采用压缩格式.true:压缩格式
     * @return 返回String类型的WIF格式私钥
     */
    public static String generatePrivateKeyWIF(String hexPrivKey, boolean flag) {
        String versionStr;
        if (flag) {
            versionStr = "80" + hexPrivKey + "01";
        } else {
            versionStr = "80" + hexPrivKey;
        }
        try {
            String hashDouble = sha256(HashUtils.hexToByteArray(sha256(HashUtils.hexToByteArray(versionStr))));
            String checkSum = hashDouble.substring(0, 8);

            String strPrivKey = versionStr + checkSum;
            return Base58.encode(HashUtils.hexToByteArray(strPrivKey));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * WIF格式私钥返回到16进制格式
     *
     * @param wifPrivKey WIF格式私钥
     * @return 返回String类型16进制私钥
     */
    public static String getHexPrivateKey(String wifPrivKey) {
        if (validateWifPrivateKey(wifPrivKey)) {
            boolean flag = true;
            if (wifPrivKey.length() == 51 && wifPrivKey.indexOf("5") == 0) {
                flag = false;
            }
            byte[] arrPrivKey = Base58.decode(wifPrivKey);
            String hexStr = HashUtils.bytesToHex(arrPrivKey);
            String result;
            if (flag) {
                result = hexStr.substring(2, hexStr.length() - 10);
            } else {
                result = hexStr.substring(2, hexStr.length() - 8);
            }
            return result;
        } else {
            LogUtils.d("传入的WIF私钥格式校验不成功，无法转换为16进制格式");
            return null;
        }
    }

    /**
     * 验证wif私钥是否有效
     *
     * @param wifPrivateKey 比特币WIF私钥
     * @return 布尔值，有效返回true，无效返回false
     */
    public static boolean validateWifPrivateKey(String wifPrivateKey) {
        byte[] arrPrivateKey = Base58.decode(wifPrivateKey);
        String hexStr = HashUtils.bytesToHex(arrPrivateKey);
        String checksum = hexStr.substring(hexStr.length() - 8);
        String versionStr = hexStr.substring(0, hexStr.length() - 8);
        try {
            String checksumNew = sha256(HashUtils.hexToByteArray(sha256(HashUtils.hexToByteArray(versionStr)))).substring(0, 8);
            if (checksum.equals(checksumNew)) {
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 将字节数组进行sha256加密
     *
     * @param byteArray 需要加密的字节数组
     * @return 返回String类型的加密结果
     */
    public static String sha256(byte[] byteArray) {
        MessageDigest messageDigest;
        String encodeStr = "";
        try {
            messageDigest = MessageDigest.getInstance("SHA-256");
            messageDigest.update(byteArray);
            encodeStr = byte2Hex(messageDigest.digest());
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return encodeStr;
    }

    /**
     * 将byte转为16进制
     *
     * @param byteArray 需要转换的byte字节数组
     * @return 返回String类型的16进制字符串
     */
    private static String byte2Hex(byte[] byteArray) {
        StringBuilder stringBuilder = new StringBuilder();
        String temp;
        for (byte b : byteArray) {
            temp = Integer.toHexString(b & 0xFF);
            if (temp.length() == 1) {
                //1得到一位的进行补0操作
                stringBuilder.append("0");
            }
            stringBuilder.append(temp);
        }
        return stringBuilder.toString();
    }
}