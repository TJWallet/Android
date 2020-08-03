package com.tianji.blockchain.entity;

import java.io.Serializable;

public class AddressEntity implements Serializable {
    private int chainType;
    private String addressName;
    private String address;
    private String addressTips;

    public int getChainType() {
        return chainType;
    }

    public void setChainType(int chainType) {
        this.chainType = chainType;
    }

    public String getAddressName() {
        return addressName;
    }

    public void setAddressName(String addressName) {
        this.addressName = addressName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getAddressTips() {
        return addressTips;
    }

    public void setAddressTips(String addressTips) {
        this.addressTips = addressTips;
    }

    @Override
    public String toString() {
        return "AddressEntity{" +
                "chainType=" + chainType +
                ", addressName='" + addressName + '\'' +
                ", address='" + address + '\'' +
                ", addressTips='" + addressTips + '\'' +
                '}';
    }
}
