package com.tianji.blockchain.entity;

import java.util.List;

public class WalletFragmentAssetsListEntity {
    private List<AssetsDetailsItemEntity> assetsList;
    /***地址拼接chain***/
    private String key;

    public List<AssetsDetailsItemEntity> getAssetsList() {
        return assetsList;
    }

    public void setAssetsList(List<AssetsDetailsItemEntity> assetsList) {
        this.assetsList = assetsList;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}
