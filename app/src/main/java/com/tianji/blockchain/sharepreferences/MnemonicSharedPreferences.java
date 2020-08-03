package com.tianji.blockchain.sharepreferences;

import android.content.Context;
import android.content.SharedPreferences;

public class MnemonicSharedPreferences {
    public static final String MNEMONIC_SP_KEY = "mnemonic_sp_key";
    private SharedPreferences.Editor mSharedPreferncesEditor;
    private SharedPreferences mSharedPrefernces;


    /**
     * 单例模式
     **/
    private static MnemonicSharedPreferences instance;

    private MnemonicSharedPreferences(Context context) {
        mSharedPrefernces = context.getSharedPreferences(MNEMONIC_SP_KEY, Context.MODE_PRIVATE);
        mSharedPreferncesEditor = mSharedPrefernces.edit();
    }

    public static MnemonicSharedPreferences getInstance(Context context) {
        if (instance == null) {
            instance = new MnemonicSharedPreferences(context);
        }
        return instance;
    }

    public boolean isBackUpMnemonic(String address) {
        boolean isBackUp = mSharedPrefernces.getBoolean(address, false);
        return isBackUp;
    }

    public void saveBackUpMnemonic(String address, boolean isBackUp) {
        mSharedPreferncesEditor.putBoolean(address, isBackUp);
        mSharedPreferncesEditor.commit();
    }
}
