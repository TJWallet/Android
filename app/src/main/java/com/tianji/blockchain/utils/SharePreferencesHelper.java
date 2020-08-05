package com.tianji.blockchain.utils;

import android.content.Context;

import com.tianji.blockchainwallet.constant.enums.Chain;
import com.tianji.blockchain.R;
import com.tianji.blockchain.entity.AssetsDetailsItemEntity;
import com.tianji.blockchain.sharepreferences.AssetsListSharedPreferences;


public class SharePreferencesHelper {
    public static void addDefaultAssets(Context context, String address, Chain chainType) {
        LogUtils.log("添加新得资产 == " + chainType);
        switch (chainType) {
            case FIL:
                AssetsDetailsItemEntity assetsEntityFileCoin = new AssetsDetailsItemEntity();
                assetsEntityFileCoin.setAssetsName(context.getResources().getString(R.string.filecoin));
                assetsEntityFileCoin.setContractAddress("");
                assetsEntityFileCoin.setAssetsIconRes(R.drawable.file_coin_select);
                assetsEntityFileCoin.setDecimals(18);
                String filKey = address + chainType;
                AssetsListSharedPreferences.getInstance(context).addAssetes(filKey, assetsEntityFileCoin);
                break;
        }
    }

}
