package com.tianji.blockchain.constant.enums;

import com.tianji.blockchain.wallet.BaseWallet;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public enum Chain implements Serializable {

    ALL("All", "ALL", 0x0, null),
    FileCoin("FileCoin", "FIL", 0x0, null);

    /**
     * 区块链名称
     */
    private String name;
    /**
     * 区块链币简写
     */
    private String symbol;
    /**
     * 区块链币编号
     */
    private int code;
    /**
     * 钱包
     */
    private BaseWallet baseWallet;

    Chain(String name, String symbol, int code, BaseWallet baseWallet) {
        this.name = name;
        this.symbol = symbol;
        this.code = code;
        this.baseWallet = baseWallet;
    }

    public String getName() {
        return name;
    }

    public String getSymbol() {
        return symbol;
    }

    public BaseWallet getWallet() {
        return baseWallet;
    }

    public static Chain getEnumByCode(int code) {
        for (Chain chain : Chain.getChainList()) {
            if (code == chain.code) {
                return chain;
            }
        }
        return null;
    }

    public static Chain getEnumByName(String name) {
        for (Chain chain : Chain.getChainList()) {
            if (name.equals(chain.getName())) {
                return chain;
            }
        }
        return null;
    }

    public static Chain getEnumBySymbol(String symbol) {
        for (Chain chain : Chain.getChainList()) {
            if (symbol.equals(chain.getSymbol())) {
                return chain;
            }
        }
        return null;
    }

    public static List<Chain> getChainList() {
        List<Chain> chainList = new ArrayList<>();
        chainList.add(FileCoin);
        return chainList;
    }
}
