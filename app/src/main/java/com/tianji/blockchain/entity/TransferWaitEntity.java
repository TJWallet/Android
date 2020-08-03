package com.tianji.blockchain.entity;

public class TransferWaitEntity {
    private String coinName;
    private TransferRecode transferRecode;
    /*** 0：交易等待中 1：交易可能遇到问题***/
    private int status;

    public String getCoinName() {
        return coinName;
    }

    public void setCoinName(String coinName) {
        this.coinName = coinName;
    }

    public TransferRecode getTransferRecode() {
        return transferRecode;
    }

    public void setTransferRecode(TransferRecode transferRecode) {
        this.transferRecode = transferRecode;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "TransferWaitEntity{" +
                "coinName='" + coinName + '\'' +
                ", transferRecode=" + transferRecode +
                ", status=" + status +
                '}';
    }
}
