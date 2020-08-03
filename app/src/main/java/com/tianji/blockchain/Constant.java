package com.tianji.blockchain;


public class Constant {
    private static final String baseUrl = "https://api.tjwallet.net/v2/";
    private static final String aclTestUrl = "http://apitest.tjwallet.net/v2/";
//    private static final String baseUrl = "http://walletapi.dapponline.ltd/v2/";
//    private static final String baseUrl = "http://apitest.tjwallet.net/v1/";
//    private static final String baseUrl = "http://192.168.1.205:8080/v1/";
//    private static final String baseUrl = "http://walletapi.dapponline.io/v1/";

    /*** 硬件初始化成功 ***/
    public static final int USB_INIT_SUCCESS = 0x61;

    /*** 语言 ***/
    public static final String LANG_ENGLISH = "en-US";

    public static final String LANGUAGE_DEFAULT = "中文";

    public static final String[] LANGUAGE_ARRAY = new String[]{
            "中文", // 中文
            "English", // 英语
    };

    /*** 是否使用模拟模式 ***/
    public static final boolean IS_MOCK_MODE = false;
    /*** 模拟 SN ***/
    public static final String MODE_SN = "SN10087";


    public static class ChainType {
        /*** 以太坊 ***/
        public static final int CHAIN_TYPE_ETH = 1;
        /*** BTC ***/
        public static final int CHAIN_TYPE_BTC = 2;
        /*** ACL ***/
        public static final int CHAIN_TYPE_ACL = 3;
    }

    /***货币种类***/
    public static class CurrencyType {
        /*** CNY ***/
        public static final int TYPE_CNY = 1;
        /*** USD ***/
        public static final int TYPE_USD = 2;
    }

    /**
     * 权限申请CODE
     */
    public static class PermissionsCode {
        public static final int CAMERA_PERMISSIONS_CODE = 0x30;
        public static final int WRITE_PERMISSION = 0x31;
    }

    /***启动地址簿得位置标识***/
    public static class AddressBookMark {
        public static final int TYPE_MAIN = 0;
        public static final int TYPE_TRANSFER = 1;
    }

    /***页面跳转来源***/
    public static class StartPageType {
        public static final int TYPE_CREATE = 1;
        public static final int TYPE_WALLETDETAILS = 2;
    }

    /***导入钱包种类***/
    public static class ImportWallet {
        public static final int IMPORTTYPE_MNEMONICS = 1;
        public static final int IMPORTTYPE_PRIVATEKEY = 2;
        public static final int IMPORTTYPE_KEYSTORE = 3;
    }

    /***地址簿类型***/
    public static class AddressType {
        public static final int TYPE_ADDRESS_CREATE = 1;
        public static final int TYPE_ADDRESS_EDIT = 2;
        public static final int TYPE_ADDRESS_OPERATE = 3;
    }

    /***创建钱包的来源**/
    public static class CreateWalletSource {
        public static final int SOURCE_MIAN_ACTIVITY = 0;
        public static final int SOURCE_HOME_ACTIVITY = 1;
    }

    /***启动钱包管理界面来源***/
    public static class StartWalletManagerSource {
        public static final int SOURCE_MAIN_ACTIVITY = 0;
        public static final int SOURCE_TRANSFER = 1;
    }

    /***创建钱包的来源**/
    public static class AddWalletSource {
        public static final int ADD_WALLET_BY_CREATE = 0;
        public static final int ADD_WALLET_BY_IMPORT_MNEMONIC = 1;
        public static final int ADD_WALLET_BY_IMPORT_PRIVATE_KEY = 2;
        public static final int ADD_WALLET_BY_IMPORT_KEY_STORE = 3;
    }

    /***各个接口请求地址***/
    public static class HttpUrl {
        /***获取问题栏目列表***/
        public static final String getQuestionColunmn = baseUrl + "qa/column";
        /***获取栏目里的问题列表***/
        public static final String getQuestionList = baseUrl + "qa/?";
        /***获取系统消息***/
        public static final String getSystemMsgList = baseUrl + "system-message/?";
        /***自更新***/
        public static final String updateUrl = baseUrl + "apkversion/lastest";
        /***查询货币估值***/
        public static final String getCoinValue = baseUrl + "eth/token/price";
        /***搜索货币名称***/
        public static final String searchCoinName = baseUrl + "eth/token/supportToken";
        /***服务协议***/
        public static final String AGGREEMENT_URL = baseUrl + "article/UserAgreement.html";
        /***隐私协议***/
        public static final String PRIVACY_URL = baseUrl + "article/PrivacyAgreement.html";
        /***获取帮助详情***/
        public static final String GET_QA_DETAIL = baseUrl + "qa/";


    }

    /*** 偏好参数KEY ***/
    public static class PreferenceKeys {
        // 偏好参数版本（版本增加会删除原先保存偏好参数，通常当保存的字段出现修改会需要增加版本）
        public static final int VERSION = 8;

        private static final String BASE = "wallet_preference:";

        public static final String FILE_NAME = "wallet_preference";

        public static final String RECENT_HISTORY = BASE + "recent_history";

        public static final String SEARCH_HISTORY = BASE + "search_history";
    }


    public static class StartActivityResultCode {
        public static final int START_ADDRESS_BOOK = 0x21;
        public static final int START_CREATE_WALLET = 0x22;
        public static final int START_WALLET_MANAGER = 0x23;
        public static final int START_EDIT_WALLET_PASSWORD = 0x24;
        public static final int START_SELECT_ASSETS = 0x25;

    }

    public static class EthGasPriceSpeed {
        public static final double TYPE_SPEED_SLOW = 1.0;
        public static final double TYPE_SPEED_NORMAL = 1.1;
        public static final double TYPE_SPEED_FAST = 1.2;
    }


}
