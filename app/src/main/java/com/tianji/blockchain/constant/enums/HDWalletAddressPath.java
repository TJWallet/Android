package com.tianji.blockchain.constant.enums;

public enum HDWalletAddressPath {

    FIL("M/44H/60H/0H/0/", 0x1);

    /**
     * HD钱包区块链地址路径
     */
    private String path;
    /**
     * HD钱包区块链地址路径编码
     */
    private int code;

    HDWalletAddressPath(String path, int code) {
        this.path = path;
        this.code = code;
    }

    public String getPath() {
        return path;
    }

    public int getCode() {
        return code;
    }

    public static HDWalletAddressPath getEnumByCode(int code) {
        for (HDWalletAddressPath hdWalletAddressPath : HDWalletAddressPath.values()) {
            if (code == hdWalletAddressPath.getCode()) {
                return hdWalletAddressPath;
            }
        }
        return null;
    }
}
