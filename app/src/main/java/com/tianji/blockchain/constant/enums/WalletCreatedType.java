package com.tianji.blockchain.constant.enums;

public enum WalletCreatedType {

    LOCAL_CREATED(0x1), IMPORTED_MNEMONIC(0x2), IMPORTED_PRIVATE_KEY(0x3), IMPORTED_KEYSTORE(0x4);

    /**
     * 钱包创建方式类型编码
     */
    private final int code;

    WalletCreatedType(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }

    public static WalletCreatedType getEnumByCode(int code) {
        for (WalletCreatedType walletCreatedType : WalletCreatedType.values()) {
            if (code == walletCreatedType.code) {
                return walletCreatedType;
            }
        }
        return null;
    }
}
