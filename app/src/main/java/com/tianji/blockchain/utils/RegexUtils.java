package com.tianji.blockchain.utils;

import java.util.regex.Pattern;


public final class RegexUtils {

    /**
     * 验证钱包密码是否合规，大小写字母数字8-20位
     *
     * @param pwd
     * @return
     */
    public static boolean walletPassword(String pwd) {
        String regex = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)[~`!@#%&*.?、,_A-Za-z\\d]{8,20}$";
        return Pattern.matches(regex, pwd);
    }

    public static boolean upWord(String str) {
        String regex = ".*?[A-Z]+?.*?";
        return Pattern.matches(regex, str);
    }

    public static boolean downWord(String str) {
        String regex = ".*?[a-z]+?.*?";
        return Pattern.matches(regex, str);
    }

    public static boolean number(String str) {
        String regex = ".*?[0-9]+?.*?";
        return Pattern.matches(regex, str);
    }


    /**
     * 检查有没有表情包
     *
     * @param string
     * @return
     */
    public static boolean checkEmoji(String string) {
        Pattern emoji = Pattern.compile("[\ud83c\udc00-\ud83c\udfff]|[\ud83d\udc00-\ud83d\udfff]|[\u2600-\u27ff]",
                Pattern.UNICODE_CASE | Pattern.CASE_INSENSITIVE);
        return emoji.matcher(string).find();
    }


    /**
     * 验证钱包名称格式是否正确
     *
     * @param walletName
     * @return
     */
    public static boolean checkWalletName(String walletName) {
        String regex = "^[~`!@#%&*.?、,_A-Za-z0-9\u4E00-\u9FA5]{0,10}";
        return Pattern.matches(regex, walletName);
    }


}
