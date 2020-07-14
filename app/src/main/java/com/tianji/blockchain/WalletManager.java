package com.tianji.blockchain;

public class WalletManager {

    private volatile static WalletManager instance;

    private WalletManager() {
    }

    public static WalletManager getInstance() {
        if (instance == null) {
            synchronized (WalletManager.class) {
                if (instance == null) {
                    instance = new WalletManager();
                }
            }
        }
        return instance;
    }
}
