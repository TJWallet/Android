package com.tianji.blockchain.util;

import android.text.TextUtils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MD5Utils {

    /**
     * 将数据进行MD5加密
     *
     * @param content 需要加密的内容
     * @return 返回加密后内容
     * @throws NoSuchAlgorithmException 加密失败时抛出异常
     */
    public static String md5(String content) throws NoSuchAlgorithmException {
        if (TextUtils.isEmpty(content)) {
            LogUtils.d("MD5加密内容为空，加密失败!!!");
            return null;
        }
        MessageDigest md5 = MessageDigest.getInstance("MD5");
        byte[] bytes = md5.digest(content.getBytes());
        String result = "";
        for (byte b : bytes) {
            String temp = Integer.toHexString(b & 0xff);
            if (temp.length() == 1) {
                temp = "0" + temp;
            }
            result += temp;
        }
        return result;
    }
}
