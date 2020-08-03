package com.tianji.blockchain.entity;

import java.io.Serializable;
import java.util.List;

public class CoinTransferRecode implements Serializable {
    private String coinName;
    private List<TransferRecode> transferRecodeList;


    public String getCoinName() {
        return coinName;
    }

    public void setCoinName(String coinName) {
        this.coinName = coinName;
    }

    public List<TransferRecode> getTransferRecodeList() {
        return transferRecodeList;
    }

    public void setTransferRecodeList(List<TransferRecode> transferRecodeList) {
        this.transferRecodeList = transferRecodeList;
    }

    @Override
    public String toString() {
        return "CoinTransferRecode{" +
                "coinName='" + coinName + '\'' +
                ", transferRecodeList=" + transferRecodeList +
                '}';
    }
}
