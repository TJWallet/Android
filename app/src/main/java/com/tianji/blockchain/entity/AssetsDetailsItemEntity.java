package com.tianji.blockchain.entity;


import java.io.Serializable;

/**
 * 钱包首页资产实体类
 */
public class AssetsDetailsItemEntity implements Serializable {
    private int id;
    private String desc;
    private int decimals;
    private long totalSupply;
    private String assetsIconUrl;
    private int assetsIconRes;
    private String assetsName;
    private String assetsAmount;
    private double assetsValue;
    private String contractAddress;
    private String assetsCompleteName;
    private String iconUrl;
    private boolean isChoosed;
    private double totalPrice;
    private double unitPrice;
    private double balance;


    public String getAssetsIconUrl() {
        return assetsIconUrl;
    }

    public void setAssetsIconUrl(String assetsIconUrl) {
        this.assetsIconUrl = assetsIconUrl;
    }

    public int getAssetsIconRes() {
        return assetsIconRes;
    }

    public void setAssetsIconRes(int assetsIconRes) {
        this.assetsIconRes = assetsIconRes;
    }

    public String getAssetsName() {
        return assetsName;
    }

    public void setAssetsName(String assetsName) {
        this.assetsName = assetsName;
    }

    public String getAssetsAmount() {
        return assetsAmount;
    }

    public void setAssetsAmount(String assetsAmount) {
        this.assetsAmount = assetsAmount;
    }

    public double getAssetsValue() {
        return assetsValue;
    }

    public void setAssetsValue(double assetsValue) {
        this.assetsValue = assetsValue;
    }

    public String getContractAddress() {
        return contractAddress;
    }

    public void setContractAddress(String contractAddress) {
        this.contractAddress = contractAddress;
    }

    public String getAssetsCompleteName() {
        return assetsCompleteName;
    }

    public void setAssetsCompleteName(String assetsCompleteName) {
        this.assetsCompleteName = assetsCompleteName;
    }

    public boolean isChoosed() {
        return isChoosed;
    }

    public void setChoosed(boolean choosed) {
        this.isChoosed = choosed;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public int getDecimals() {
        return decimals;
    }

    public void setDecimals(int decimals) {
        this.decimals = decimals;
    }

    public long getTotalSupply() {
        return totalSupply;
    }

    public void setTotalSupply(long totalSupply) {
        this.totalSupply = totalSupply;
    }

    public String getIconUrl() {
        return iconUrl;
    }

    public void setIconUrl(String iconUrl) {
        this.iconUrl = iconUrl;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public double getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(double unitPrice) {
        this.unitPrice = unitPrice;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    @Override
    public String toString() {
        return "AssetsDetailsItemEntity{" +
                "id=" + id +
                ", desc='" + desc + '\'' +
                ", decimals=" + decimals +
                ", totalSupply=" + totalSupply +
                ", assetsIconUrl='" + assetsIconUrl + '\'' +
                ", assetsIconRes=" + assetsIconRes +
                ", assetsName='" + assetsName + '\'' +
                ", assetsAmount='" + assetsAmount + '\'' +
                ", assetsValue=" + assetsValue +
                ", contractAddress='" + contractAddress + '\'' +
                ", assetsCompleteName='" + assetsCompleteName + '\'' +
                ", iconUrl='" + iconUrl + '\'' +
                ", isChoosed=" + isChoosed +
                ", totalPrice=" + totalPrice +
                ", unitPrice=" + unitPrice +
                ", balance=" + balance +
                '}';
    }
}
