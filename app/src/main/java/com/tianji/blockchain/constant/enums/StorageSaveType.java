package com.tianji.blockchain.constant.enums;

public enum StorageSaveType {
    LOCAL(0x1);

    /**
     * 钱包信息存储编码
     */
    private final int code;

    StorageSaveType(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }

    public static StorageSaveType getEnumByCode(int code) {
        for (StorageSaveType storageSaveType : StorageSaveType.values()) {
            if (code == storageSaveType.code) {
                return storageSaveType;
            }
        }
        return null;
    }
}
