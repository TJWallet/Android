package com.tianji.blockchain.entity;

import com.tianji.blockchainwallet.entity.WalletInfo;

import java.io.Serializable;
import java.util.List;

public class AllObWalletEntity implements Serializable {
    private String usbMacAddress;
    private List<WalletInfo> walletInfoList;
    private boolean isSelected;
    private String hardwareName;
    private String uuid;
    private long timeStamp;
    private long usbInitTimeStamp;


    public String getUsbMacAddress() {
        return usbMacAddress;
    }

    public void setUsbMacAddress(String usbMacAddress) {
        this.usbMacAddress = usbMacAddress;
    }

    public List<WalletInfo> getWalletInfoList() {
        return walletInfoList;
    }

    public void setWalletInfoList(List<WalletInfo> walletInfoList) {
        this.walletInfoList = walletInfoList;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        this.isSelected = selected;
    }

    public String getHardwareName() {
        return hardwareName;
    }

    public void setHardwareName(String hardwareName) {
        this.hardwareName = hardwareName;
    }

    public long getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(long timeStamp) {
        this.timeStamp = timeStamp;
    }

    public long getUsbInitTimeStamp() {
        return usbInitTimeStamp;
    }

    public void setUsbInitTimeStamp(long usbInitTimeStamp) {
        this.usbInitTimeStamp = usbInitTimeStamp;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    @Override
    public String toString() {
        return "AllObWalletEntity{" +
                "usbMacAddress='" + usbMacAddress + '\'' +
                ", walletInfoList=" + walletInfoList +
                ", isSelected=" + isSelected +
                ", hardwareName='" + hardwareName + '\'' +
                ", uuid='" + uuid + '\'' +
                ", timeStamp=" + timeStamp +
                ", usbInitTimeStamp=" + usbInitTimeStamp +
                '}';
    }
}
