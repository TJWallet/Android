package com.tianji.blockchain.constant.enums;

public enum ResultCode {

    SUCCESS(0x101, "成功"),
    FAIL(0x102, "失败"),

    PARAMS_ERROR(0x103, "参数错误"),
    CHAIN_NOT_EXISTS(0x104, "区块链不存在"),
    KEYSTORE_NOT_SUPPORT(0x105, "非Keystore支持类型"),
    ACCOUNT_BALANCE_NOT_ENOUGH(0x106, "账户余额不足"),

    WALLET_FILE_NOT_EXISTS(0x301, "钱包文件不存在"),
    WALLET_FILE_CONTENT_ERROR(0x302, "钱包文件内容错误"),
    WALLET_FILE_PASSWORD_ERROR(0x303, "钱包文件密码不正确"),
    WALLET_FILE_ENCRYPT_FAIL(0x304, "钱包文件加密失败"),
    WALLET_FILE_ADDRESS_EXISTS(0x305, "钱包地址已经存在"),
    WALLET_FILE_REMARKS_EXISTS(0x306, "钱包备注已经存在"),
    WALLET_FILE_NAME_EXISTS(0x307, "钱包名称已经存在"),
    WALLET_FILE_KEYSTORE_PWD_ERROR(0x308, "钱包Keystore密码不正确");

    /**
     * 错误码
     */
    private final int code;
    /**
     * 错误信息
     */
    private String errorMsg;

    ResultCode(int code, String errorMsg) {
        this.code = code;
        this.errorMsg = errorMsg;
    }

    public int getCode() {
        return code;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public static ResultCode getEnumByCode(int code) {
        for (ResultCode resultCode : ResultCode.values()) {
            if (code == resultCode.code) {
                return resultCode;
            }
        }
        return null;
    }
}
