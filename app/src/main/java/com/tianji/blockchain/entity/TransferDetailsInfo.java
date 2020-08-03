package com.tianji.blockchain.entity;

public class TransferDetailsInfo {
    private boolean isTransferInfo;
    private String address;
    private long time;
    private String timeStr;
    private double amount;


    public boolean isTransferInfo() {
        return isTransferInfo;
    }

    public void setTransferInfo(boolean transferInfo) {
        isTransferInfo = transferInfo;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public String getTimeStr() {
        return timeStr;
    }

    public void setTimeStr(String timeStr) {
        this.timeStr = timeStr;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    @Override
    public String toString() {
        return "TransferDetailsInfo{" +
                "isTransferInfo=" + isTransferInfo +
                ", address='" + address + '\'' +
                ", time=" + time +
                ", timeStr='" + timeStr + '\'' +
                ", amount=" + amount +
                '}';
    }
}
