package com.tianji.blockchain.constant.enums;

public enum WalletProof {
    MNEMONIC(0x1), PRIVATE_KEY(0x2), KEYSTORE(0x3);

    /**
     * 凭证类型
     */
    private final int code;

    WalletProof(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }

    public static WalletProof getEnumByCode(int code) {
        for (WalletProof walletProof : WalletProof.values()) {
            if (code == walletProof.code) {
                return walletProof;
            }
        }
        return null;
    }
}
