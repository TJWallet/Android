package com.tianji.blockchain.entity;

import android.graphics.Bitmap;

import java.io.Serializable;

/**
 * 收款实体类
 */
public class CollectEntity implements Serializable {
    private Bitmap coinIconBitmap;
    private String coinName;
    private String collectAddress;

    public Bitmap getCoinIconBitmap() {
        return coinIconBitmap;
    }

    public void setCoinIconBitmap(Bitmap coinIconId) {
        this.coinIconBitmap = coinIconId;
    }

    public String getCoinName() {
        return coinName;
    }

    public void setCoinName(String coinName) {
        this.coinName = coinName;
    }

    public String getCollectAddress() {
        return collectAddress;
    }

    public void setCollectAddress(String collectAddress) {
        this.collectAddress = collectAddress;
    }

    @Override
    public String toString() {
        return "CollectEntity{" +
                "coinIconId=" + coinIconBitmap +
                ", coinName='" + coinName + '\'' +
                ", collectAddress='" + collectAddress + '\'' +
                '}';
    }
}
