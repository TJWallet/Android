package com.tianji.blockchain.constant.enums;

import java.io.Serializable;

public enum WalletDir implements Serializable {

    NORMAL("wallets"), BAK_LOCAL("wallets_bak_local"), BAK_EXTERNAL("wallets_bak_external");

    /**
     * 备份文件夹名称
     */
    private String name;

    WalletDir(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
