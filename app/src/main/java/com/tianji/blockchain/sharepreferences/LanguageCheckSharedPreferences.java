package com.tianji.blockchain.sharepreferences;

import android.content.Context;
import android.content.SharedPreferences;


public class LanguageCheckSharedPreferences {

    public static final String LANG_SP_KEY = "lang_sp_key";
    private SharedPreferences.Editor mSharedPreferncesEditor;
    private SharedPreferences mSharedPrefernces;
    private Context context;

    /**
     * 单例模式
     **/
    private static LanguageCheckSharedPreferences instance;

    private LanguageCheckSharedPreferences(Context context) {
        this.context = context;
        mSharedPrefernces = context.getSharedPreferences(LANG_SP_KEY, Context.MODE_PRIVATE);
        mSharedPreferncesEditor = mSharedPrefernces.edit();
    }

    public static LanguageCheckSharedPreferences getInstance(Context context) {
        if (instance == null) {
            instance = new LanguageCheckSharedPreferences(context);
        }
        return instance;
    }

    public void changeLanguageCheck() {
        mSharedPreferncesEditor.putBoolean(LANG_SP_KEY, true).commit();
    }

    public boolean getLanguageCheck() {
        return mSharedPrefernces.getBoolean(LANG_SP_KEY, false);
    }
}
