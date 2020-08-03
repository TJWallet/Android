package com.tianji.blockchain.sharepreferences;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.tianji.blockchainwallet.entity.WalletInfo;

public class CurrentWalletSharedPreferences {
    public static final String CURRENT_WALLET_KEY = "currentWallet_sp_key";
    private SharedPreferences.Editor mSharedPreferncesEditor;
    private SharedPreferences mSharedPrefernces;

    /**
     * 单例模式
     **/
    private static CurrentWalletSharedPreferences instance;

    public Gson gson;

    private CurrentWalletSharedPreferences(Context context) {
        mSharedPrefernces = context.getSharedPreferences(CURRENT_WALLET_KEY, Context.MODE_PRIVATE);
        mSharedPreferncesEditor = mSharedPrefernces.edit();
        gson = new Gson();
    }

    public static CurrentWalletSharedPreferences getInstance(Context context) {
        if (instance == null) {
            if (context != null) {
                instance = new CurrentWalletSharedPreferences(context);
            }
        }
        return instance;
    }


    /**
     * 更改当前钱包的缓存记录
     */
    public void changeCurrentWallet(WalletInfo walletInfo) {
        String record = gson.toJson(walletInfo);
        mSharedPreferncesEditor.putString(CURRENT_WALLET_KEY, record);
        mSharedPreferncesEditor.commit();
    }

    /**
     * 清除所有的当前钱包缓存记录
     */
    public void deleteCurrentWallet() {
        mSharedPreferncesEditor.clear();
        mSharedPreferncesEditor.commit();
    }

    /**
     * 获取当前缓存钱包的地址
     *
     * @return
     */
    public WalletInfo getCurrentWallet() {
        String record = mSharedPrefernces.getString(CURRENT_WALLET_KEY, "");
        if (record.equals("")) {
            return null;
        } else {
            WalletInfo walletInfo = gson.fromJson(record, WalletInfo.class);
            return walletInfo;
        }
    }

}
