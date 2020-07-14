package com.tianji.blockchain.constant.enums;

import java.io.Serializable;

public enum TransferType implements Serializable {

    FILECOIN(0x1);

    /**
     * 交易类型码
     */
    private final int code;

    TransferType(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }

    public static TransferType getEnumByCode(int code) {
        for (TransferType transferType : TransferType.values()) {
            if (code == transferType.code) {
                return transferType;
            }
        }
        return null;
    }
}
