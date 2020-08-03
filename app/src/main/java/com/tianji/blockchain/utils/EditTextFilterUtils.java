package com.tianji.blockchain.utils;

import android.text.InputFilter;
import android.text.Spanned;
import android.widget.EditText;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class EditTextFilterUtils {
    public static void setFilter(EditText editText, InputFilter filter) {
        InputFilter[] filters = {filter};
        editText.setFilters(filters);
    }

    private static boolean match(String regex, String str) {
        if (str.equals("")) {
            return false;
        }
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(str);
        return matcher.matches();
    }


    public static class EditNoEnterFilter implements InputFilter {

        @Override
        public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
            if (source.equals("")) {
                return "";
            }
            LogUtils.log("收到的输入 = " + source.toString());
            String str = source.toString();
            str = str.replace("\n", "");

            return str;
        }
    }

    public static class EditNoEnterNoSpaceFilter implements InputFilter {

        @Override
        public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
            if (source.equals("")) {
                return "";
            }

            String str = source.toString();
            str = str.replace(" ", "");
            str = str.replace("\n", "");

            return str;
        }
    }

}
