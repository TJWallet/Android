package com.tianji.blockchain.entity;

import java.io.Serializable;

public class TransferEntity implements Serializable {
    private double tansferAmount;
    private int chainType;
    private String transferIntoAddress;
    private String transferOutAddress;
    private double minersFee;

    public double getTansferAmount() {
        return tansferAmount;
    }

    public void setTansferAmount(double tansferAmount) {
        this.tansferAmount = tansferAmount;
    }

    public int getChainType() {
        return chainType;
    }

    public void setChainType(int chainType) {
        this.chainType = chainType;
    }

    public String getTransferIntoAddress() {
        return transferIntoAddress;
    }

    public void setTransferIntoAddress(String transferIntoAddress) {
        this.transferIntoAddress = transferIntoAddress;
    }

    public String getTransferOutAddress() {
        return transferOutAddress;
    }

    public void setTransferOutAddress(String transferOutAddress) {
        this.transferOutAddress = transferOutAddress;
    }

    public double getMinersFee() {
        return minersFee;
    }

    public void setMinersFee(double minersFee) {
        this.minersFee = minersFee;
    }

    @Override
    public String toString() {
        return "TransferEntity{" +
                "tansferAmount=" + tansferAmount +
                ", chainType=" + chainType +
                ", transferIntoAddress='" + transferIntoAddress + '\'' +
                ", transferOutAddress='" + transferOutAddress + '\'' +
                ", minersFee=" + minersFee +
                '}';
    }
}
