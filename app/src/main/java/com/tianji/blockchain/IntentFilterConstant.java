package com.tianji.blockchain;

public class IntentFilterConstant {
    /**
     * 选中钱包，重新加载页面
     */
    public static final String RELOAD_WALLET_FRAGMENT = "com.tianji.RELOAD_WALLET_FRAGMENT";
    /**
     * mainactivity的启动类型
     */
    public static final String MAINACTIVITY_START_TYPE = "com.tianji.MAINACTIVITY_START_TYPE";
    /**
     * 更换资产类型(货币类型)
     */
    public static final String CHANGE_ASSETS_TYPE = "com.tianji.CHANGE_ASSETS_TYPE";
    /**
     * 首页获取到扫描二维码内容
     */
    public static final String MAIN_ACTIVITY_GET_QRCODE_RESULT = "com.tianji.MAIN_ACTIVITY_GET_QRCODE_RESULT";

    /**
     * 首页资产列表估值获取完成
     */
    public static final String ASSETS_VALUE_GET_FINISH = "com.tianji.ASSETS_VALUE_GET_FINISH";

    /**
     * 关于USB监听的内部广播
     */
    public static class UsbIntentFilter {
        public static final String USB_CONNECT = "com.tianji.USB_CONNECT";
        public static final String USB_DISCONNECT = "com.tianji.USB_DISCONNECT";
        public static final String USB_IS_CONNECTED = "com.tianji.USB_IS_CONNECTED";
        public static final String USB_NOT_CONNECTED = "com.tianji.USB_NOT_CONNECTED";
        public static final String GET_USB_CONNECTED = "com.tianji.GET_USB_CONNECTED";
    }

    /**
     * 请求IPFS地址列表的广播
     */
    public static class IpfsIntentFilter {
        public static final String GET_IPFS_ADDRESS_LIST_START = "com.tianji.GET_IPFS_ADDRESS_LIST_START";
        public static final String GET_IPFS_ADDRESS_LIST_SUCCESS = "com.tianji.GET_IPFS_ADDRESS_LIST_SUCCESS";
        public static final String GET_IPFS_ADDRESS_LIST_FAIL = "com.tianji.GET_IPFS_ADDRESS_LIST_FAIL";
    }

}
