package com.tianji.blockchain.sharepreferences;

import android.content.Context;
import android.content.SharedPreferences;


import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.tianji.blockchainwallet.entity.WalletInfo;
import com.tianji.blockchain.entity.AllObWalletEntity;
import com.tianji.blockchain.entity.AssetsDetailsItemEntity;
import com.tianji.blockchain.utils.LogUtils;
import com.tianji.blockchain.utils.WalletListHelper;


import java.util.ArrayList;
import java.util.List;

public class AssetsListSharedPreferences {
    public static final String ASSETS_SP_KEY = "assets_sp_key";
    private int mVersion = 3;
    private SharedPreferences mSharedPrefernces;
    private SharedPreferences.Editor mSharedPreferncesEditor;
    public Gson gson;

    private Context context;

    /**
     * 单例模式
     **/
    private static AssetsListSharedPreferences instance;

    private AssetsListSharedPreferences(Context context) {
        mSharedPrefernces = context.getSharedPreferences(ASSETS_SP_KEY, Context.MODE_PRIVATE);
        mSharedPreferncesEditor = mSharedPrefernces.edit();
        gson = new Gson();
        this.context = context;
    }

    public static AssetsListSharedPreferences getInstance(Context context) {
        if (instance == null) {
            if (context != null) {
                instance = new AssetsListSharedPreferences(context);
            }
        }
        return instance;
    }

    public void checkVersion() {
        String key = ASSETS_SP_KEY + ":version";
        if (this.mSharedPrefernces.contains(key)) {
            int version = this.mSharedPrefernces.getInt(key, 1);
            LogUtils.log("AssetsListSharedPreferences 当前版本是 = " + version);
            if (version < mVersion) {
                mSharedPreferncesEditor.putInt(key, mVersion).commit();
                setNewAssets();
            }

        } else {
            mSharedPreferncesEditor.putInt(key, mVersion).commit();
            setNewAssets();
        }
    }

    private String getRecord(String key) {
        String record = mSharedPrefernces.getString(key, "");
        return record;
    }

    /**
     * 删除资产
     */
    public void removeAssets(String key, AssetsDetailsItemEntity entity) {
        String record = getRecord(key);
        if (record.equals("")) return;

        List<AssetsDetailsItemEntity> assetsDetailsItemEntityList = gson.fromJson(record, new TypeToken<List<AssetsDetailsItemEntity>>() {
        }.getType());
        for (int i = 0; i < assetsDetailsItemEntityList.size(); i++) {
            if (assetsDetailsItemEntityList.get(i).getAssetsName().equals(entity.getAssetsName())) {
                assetsDetailsItemEntityList.remove(i);
            }
        }
        String newRecord = gson.toJson(assetsDetailsItemEntityList);
        mSharedPreferncesEditor.putString(key, newRecord).commit();
    }

    /**
     * 添加资产
     */
    public void addAssetes(String key, AssetsDetailsItemEntity entity) {

        String record = getRecord(key);

        boolean hasEntity = false;

        if (record.equals("")) {
            List<AssetsDetailsItemEntity> assetsDetailsItemEntityList = new ArrayList<>();
            assetsDetailsItemEntityList.add(entity);
            String newRecord = gson.toJson(assetsDetailsItemEntityList);
            mSharedPreferncesEditor.putString(key, newRecord).commit();
        } else {
            List<AssetsDetailsItemEntity> assetsDetailsItemEntityList = gson.fromJson(record, new TypeToken<List<AssetsDetailsItemEntity>>() {
            }.getType());
            for (int i = 0; i < assetsDetailsItemEntityList.size(); i++) {
                if (assetsDetailsItemEntityList.get(i).getAssetsName().equals(entity.getAssetsName())) {
                    assetsDetailsItemEntityList.remove(i);
                    assetsDetailsItemEntityList.add(i, entity);
                    hasEntity = true;
                    break;
                } else {
                    hasEntity = false;
                }
            }
            if (!hasEntity) {
                assetsDetailsItemEntityList.add(entity);
            }

            String newRecord = gson.toJson(assetsDetailsItemEntityList);
            mSharedPreferncesEditor.putString(key, newRecord).commit();
        }
    }


    /**
     * 获取资产列表
     *
     * @return
     */
    public List<AssetsDetailsItemEntity> getAssetsList(String key) {
        List<AssetsDetailsItemEntity> lists = new ArrayList<>();

        String record = getRecord(key);

        if (record.equals("")) {
            return lists;
        } else {
            lists = gson.fromJson(record, new TypeToken<List<AssetsDetailsItemEntity>>() {
            }.getType());
            return lists;
        }
    }

    /**
     * 清空用户全部资产数据
     */
    public void deleteAllRecord() {
        mSharedPreferncesEditor.clear().commit();
    }

    private void setNewAssets() {
        //获取该钱包的所有软件钱包
        List<WalletInfo> softWalletInfoList = WalletListHelper.getInstance(context).getSoftwareWalletInfoListAll();
        for (int i = 0; i < softWalletInfoList.size(); i++) {
            String key = softWalletInfoList.get(i).getAddress() + softWalletInfoList.get(i).getChain();
//            LogUtils.log("获取到以前缓存的key = " + key);
            //获取软件钱包下的所有原本的缓存
            List<AssetsDetailsItemEntity> assetsDetailsItemEntityList = getAssetsList(key);
//            LogUtils.log("获取到以前缓存的资产列表是 = " + assetsDetailsItemEntityList.toString());
            mSharedPreferncesEditor.remove(key).commit();
            for (int j = 0; j < assetsDetailsItemEntityList.size(); j++) {
                //资产重新缓存
                addAssetes(key, assetsDetailsItemEntityList.get(j));
            }
        }
        List<AllObWalletEntity> allObWalletEntityList = ObserverWalletListSharedPreferences.getInstance(context).getAllObWalletList();
        for (int i = 0; i < allObWalletEntityList.size(); i++) {
            for (int j = 0; j < allObWalletEntityList.get(i).getWalletInfoList().size(); j++) {
                String key = allObWalletEntityList.get(i).getWalletInfoList().get(j).getAddress() + allObWalletEntityList.get(i).getWalletInfoList().get(j).getChain();
                List<AssetsDetailsItemEntity> assetsDetailsItemEntityList = getAssetsList(key);
                mSharedPreferncesEditor.remove(key).commit();
                for (int k = 0; k < assetsDetailsItemEntityList.size(); k++) {
                    //资产重新缓存
                    addAssetes(key, assetsDetailsItemEntityList.get(k));
                }
            }
        }
    }
}
