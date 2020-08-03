package com.tianji.blockchain.entity;

import java.io.Serializable;
import java.util.List;

public class WalletEntity implements Serializable {
    private String walletName;
    private String coinName;
    private String walletAddress;
    private Double coinAmount;
    private int chainType;
    private boolean isSelected;
    private List<AssetsDetailsItemEntity> assetsDetailsItemEntityList;


    public String getWalletName() {
        return walletName;
    }

    public void setWalletName(String walletName) {
        this.walletName = walletName;
    }

    public String getCoinName() {
        return coinName;
    }

    public void setCoinName(String coinName) {
        this.coinName = coinName;
    }

    public String getWalletAddress() {
        return walletAddress;
    }

    public void setWalletAddress(String walletAddress) {
        this.walletAddress = walletAddress;
    }

    public Double getCoinAmount() {
        return coinAmount;
    }

    public void setCoinAmount(Double coinAmount) {
        this.coinAmount = coinAmount;
    }

    public int getChainType() {
        return chainType;
    }

    public void setChainType(int chainType) {
        this.chainType = chainType;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public List<AssetsDetailsItemEntity> getAssetsDetailsItemEntityList() {
        return assetsDetailsItemEntityList;
    }

    public void setAssetsDetailsItemEntityList(List<AssetsDetailsItemEntity> assetsDetailsItemEntityList) {
        this.assetsDetailsItemEntityList = assetsDetailsItemEntityList;
    }

    @Override
    public String toString() {
        return "WalletEntity{" +
                "walletName='" + walletName + '\'' +
                ", coinName='" + coinName + '\'' +
                ", walletAddress='" + walletAddress + '\'' +
                ", coinAmount=" + coinAmount +
                ", chainType=" + chainType +
                ", isSelected=" + isSelected +
                ", assetsDetailsItemEntityList=" + assetsDetailsItemEntityList +
                '}';
    }
}
