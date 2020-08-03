package com.tianji.blockchain.sharepreferences;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.tianji.blockchain.entity.CoinTransferRecode;

import java.util.ArrayList;
import java.util.List;

public class AssetsDetailsSharedPreferences {
    public static final String ASSETS_DETAILS_KEY = "assets_details_sp_key";
    private SharedPreferences.Editor mSharedPreferncesEditor;
    private SharedPreferences mSharedPrefernces;

    /**
     * 单例模式
     **/
    private static AssetsDetailsSharedPreferences instance;

    public Gson gson;

    private AssetsDetailsSharedPreferences(Context context) {
        mSharedPrefernces = context.getSharedPreferences(ASSETS_DETAILS_KEY, Context.MODE_PRIVATE);
        mSharedPreferncesEditor = mSharedPrefernces.edit();
        gson = new Gson();
    }

    public static AssetsDetailsSharedPreferences getInstance(Context context) {
        if (instance == null) {
            if (context != null) {
                instance = new AssetsDetailsSharedPreferences(context);
            }
        }
        return instance;
    }

    /**
     * 获取当前记录的JSON
     *
     * @param walletAddress
     * @return
     */
    private String getRecord(String walletAddress) {
        String record = mSharedPrefernces.getString(walletAddress, "");
        return record;
    }

    public void addAssetsDetails(String walletAddress, CoinTransferRecode coinTransferRecode) {
        String record = getRecord(walletAddress);

        if (record.equals("")) {
            List<CoinTransferRecode> coinTransferRecodeList = new ArrayList<>();
            coinTransferRecodeList.add(coinTransferRecode);
            String newRecord = gson.toJson(coinTransferRecodeList);

            mSharedPreferncesEditor.putString(walletAddress, newRecord);
            mSharedPreferncesEditor.commit();
        } else {
            List<CoinTransferRecode> coinTransferRecodeList = gson.fromJson(record, new TypeToken<List<CoinTransferRecode>>() {
            }.getType());
            coinTransferRecodeList.add(coinTransferRecode);
            String newRecord = gson.toJson(coinTransferRecodeList);

            mSharedPreferncesEditor.putString(walletAddress, newRecord);
            mSharedPreferncesEditor.commit();
        }
    }

    public void removeRecord(String walletAddress, String assectName) {
        String record = getRecord(walletAddress);

        if (record.equals("")) {
            List<CoinTransferRecode> coinTransferRecodeList = new ArrayList<>();
            String newRecord = gson.toJson(coinTransferRecodeList);

            mSharedPreferncesEditor.putString(walletAddress, newRecord);
            mSharedPreferncesEditor.commit();
        } else {
            List<CoinTransferRecode> coinTransferRecodeList = gson.fromJson(record, new TypeToken<List<CoinTransferRecode>>() {
            }.getType());
            for (int i = 0; i < coinTransferRecodeList.size(); i++) {
                if (assectName.equals(coinTransferRecodeList.get(i).getCoinName())) {
                    coinTransferRecodeList.remove(i);
                }
            }

            String newRecord = gson.toJson(coinTransferRecodeList);

            mSharedPreferncesEditor.putString(walletAddress, newRecord);
            mSharedPreferncesEditor.commit();
        }
    }

    public List<CoinTransferRecode> getCoinTransferRecodeList(String walletAddress) {
        String record = getRecord(walletAddress);
        if (record.equals("")) {
            List<CoinTransferRecode> coinTransferRecodeList = new ArrayList<>();
            return coinTransferRecodeList;
        } else {
            List<CoinTransferRecode> coinTransferRecodeList = gson.fromJson(record, new TypeToken<List<CoinTransferRecode>>() {
            }.getType());
            return coinTransferRecodeList;
        }
    }
}
