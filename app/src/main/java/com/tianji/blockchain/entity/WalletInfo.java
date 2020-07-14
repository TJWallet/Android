package com.tianji.blockchain.entity;

import com.alibaba.fastjson.JSONObject;
import com.tianji.blockchain.constant.enums.Chain;
import com.tianji.blockchain.constant.enums.HDWalletAddressPath;
import com.tianji.blockchain.constant.enums.StorageSaveType;
import com.tianji.blockchain.constant.enums.WalletCreatedType;
import com.tianji.blockchain.constant.enums.WalletProof;

import java.io.Serializable;

public class WalletInfo implements Serializable {
    // 地址
    private String address;
    // 密码提示
    private String passwordTips;
    // 钱包名
    private String walletName;
    // 钱包创建方式
    private WalletCreatedType walletCreatedType;
    // 钱包所属区块链
    private Chain chain;
    // 存储类型
    private StorageSaveType storageSaveType;
    // 钱包创建日期
    private long createdTime;
    // 地址类型
    private HDWalletAddressPath hdWalletAddressPath;
    // 地址类型
    private int childAccountPos;
    // 钱包凭证类型
    private WalletProof walletProof;
    // 是否被选中
    private boolean isSelected;

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPasswordTips() {
        return passwordTips;
    }

    public void setPasswordTips(String passwordTips) {
        this.passwordTips = passwordTips;
    }

    public String getWalletName() {
        return walletName;
    }

    public void setWalletName(String walletName) {
        this.walletName = walletName;
    }

    public WalletCreatedType getWalletCreatedType() {
        return walletCreatedType;
    }

    public void setWalletCreatedType(WalletCreatedType walletCreatedType) {
        this.walletCreatedType = walletCreatedType;
    }

    public Chain getChain() {
        return chain;
    }

    public void setChain(Chain chain) {
        this.chain = chain;
    }

    public StorageSaveType getStorageSaveType() {
        return storageSaveType;
    }

    public void setStorageSaveType(StorageSaveType storageSaveType) {
        this.storageSaveType = storageSaveType;
    }

    public long getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(long createdTime) {
        this.createdTime = createdTime;
    }

    public HDWalletAddressPath getHdWalletAddressPath() {
        return hdWalletAddressPath;
    }

    public void setHdWalletAddressPath(HDWalletAddressPath hdWalletAddressPath) {
        this.hdWalletAddressPath = hdWalletAddressPath;
    }

    public WalletProof getWalletProof() {
        return walletProof;
    }

    public void setWalletProof(WalletProof walletProof) {
        this.walletProof = walletProof;
    }

    public int getChildAccountPos() {
        return childAccountPos;
    }

    public void setChildAccountPos(int childAccountPos) {
        this.childAccountPos = childAccountPos;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        this.isSelected = selected;
    }

    @Override
    public String toString() {
        return "WalletInfo{" +
                "address='" + address + '\'' +
                ", passwordTips='" + passwordTips + '\'' +
                ", walletName='" + walletName + '\'' +
                ", walletCreatedType=" + walletCreatedType +
                ", chain =" + chain +
                ", storageSaveType=" + storageSaveType +
                ", createdTime=" + createdTime +
                ", hdWalletAddressPath=" + hdWalletAddressPath +
                ", childAccountPos=" + childAccountPos +
                ", walletProof=" + walletProof +
                '}';
    }

    public JSONObject toJSONObject() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("address", address);
        jsonObject.put("passwordTips", passwordTips);
        jsonObject.put("walletName", walletName);
        jsonObject.put("walletCreatedType", walletCreatedType);
        jsonObject.put("chain", chain);
        jsonObject.put("storageSaveType", storageSaveType);
        jsonObject.put("createdTime", createdTime);
        jsonObject.put("hdWalletAddressPath", hdWalletAddressPath);
        jsonObject.put("childAccountPos", childAccountPos);
        jsonObject.put("walletProof", walletProof);
        return jsonObject;
    }
}
