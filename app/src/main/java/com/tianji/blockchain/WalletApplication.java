package com.tianji.blockchain;

import android.app.Application;
import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.view.Display;

import androidx.appcompat.app.AppCompatActivity;

import com.nostra13.universalimageloader.cache.memory.impl.UsingFreqLimitedMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.download.BaseImageDownloader;
import com.tianji.blockchainwallet.WalletManager;
import com.tianji.blockchainwallet.constant.enums.ResultCode;
import com.tianji.blockchainwallet.entity.WalletInfo;
import com.tianji.blockchainwallet.usb.IUsbAttachListener;
import com.tianji.blockchainwallet.wallet.IRequestListener;
import com.tianji.blockchain.activity.aboutus.SettingActivity;
import com.tianji.blockchain.activity.basic.UsbCallbackListener;
import com.tianji.blockchain.restful.Api;
import com.tianji.blockchain.sharepreferences.CommonSharedPreferences;
import com.tianji.blockchain.sharepreferences.LanguageCheckSharedPreferences;
import com.tianji.blockchain.utils.LogUtils;
import com.tianji.blockchain.utils.PreferencesUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class WalletApplication extends Application {
    public static String lang = "";
    public static WalletApplication app;
    /***当前显示的钱包***/
    private static WalletInfo currentWallet;
    /***当前的硬件序列号，插入硬件才会有值***/
    private static String currentUsbMac;
    /***是否是开发者模式***/
    private boolean isDeveloperVersion = false;
    /***当前货币种类***/
    private int currentCurrency = Constant.CurrencyType.TYPE_CNY;
    /***是否隐藏资产***/
    private boolean assetsIsHidden = false;
    /***是否检查过升级了***/
    private static boolean isCheckUpdata = false;
    /***现在的联网状态***/
    private boolean isNetWork = false;
    /***联网状态提示是否已经弹窗***/
    private boolean isNetWorkFirstShowed = false;
    /***记录ACL测试服状态***/
    private boolean aclTest = false;

    public boolean isAclTest() {
        return aclTest;
    }

    public boolean isDeveloperVersion() {
        return isDeveloperVersion;
    }

//    public void setAclTest(boolean aclTest) {
//        this.aclTest = aclTest;
//    }

    public void setDeveloperVersion(boolean developerVersion) {
        isDeveloperVersion = developerVersion;
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        app = this;
        // 初始化ImageLoader
        initImageLoader(this);
        // 初始化API
        Api.getInstance().init(this);
        /***APP第一次启动语言跟随系统***/
        if (!LanguageCheckSharedPreferences.getInstance(this).getLanguageCheck()) {
            LogUtils.log("当前系统语言是 = " + getResources().getConfiguration().locale.getLanguage());
            Configuration config = getResources().getConfiguration(); // 获得设置对象    
            if (getResources().getConfiguration().locale.getLanguage().equals("zh")) {
                config.locale = Locale.CHINESE;// 设置APP语言设置为中文
                PreferencesUtils.putString(this, "appLanguage", Constant.LANGUAGE_ARRAY[0]);
            } else {
                config.locale = Locale.ENGLISH;// 设置APP语言设置为英文
                PreferencesUtils.putString(this, "appLanguage", Constant.LANGUAGE_ARRAY[1]);
            }
            LanguageCheckSharedPreferences.getInstance(this).changeLanguageCheck();
        }
        //获取当前语言
        getSetLocale(this);
        //获取当前模式是否是开发者模式
        isDeveloperVersion = CommonSharedPreferences.getInstance(this).getDeveloperMode();
        //获取当前的货币种类
        currentCurrency = CommonSharedPreferences.getInstance(this).getCurrency();
//        //获取当前ACL是否是测试服
//        aclTest = CommonSharedPreferences.getInstance(this).getAclTest();
    }

    public int getCurrentCurrency() {
        return currentCurrency;
    }

    public void setCurrentCurrency(int currentCurrency) {
        this.currentCurrency = currentCurrency;
    }

    public boolean isAssetsIsHidden() {
        return assetsIsHidden;
    }

    public void setAssetsIsHidden(boolean assetsIsHidden) {
        this.assetsIsHidden = assetsIsHidden;
    }

    public static boolean isCheckUpdata() {
        return isCheckUpdata;
    }

    public static void setCheckUpdata(boolean checkUpdata) {
        isCheckUpdata = checkUpdata;
    }

    public static void setCurrentWallet(WalletInfo wallet) {
        WalletApplication.currentWallet = wallet;
        WalletApplication.currentWallet.setSelected(true);
    }

    public boolean isNetWorkFirstShowed() {
        return isNetWorkFirstShowed;
    }

    public void setNetWorkFirstShowed(boolean netWorkFirstShowed) {
        this.isNetWorkFirstShowed = netWorkFirstShowed;
    }

    public boolean isNetWork() {
        return isNetWork;
    }

    public void setNetWork(boolean netWork) {
        isNetWork = netWork;
    }

    public static WalletInfo getCurrentWallet() {
        return currentWallet;
    }

    public static String getCurrentUsbMac() {
        return currentUsbMac;
    }

    public static void setCurrentUsbMac(String currentUsbMac) {
        WalletApplication.currentUsbMac = currentUsbMac;
    }

    /**
     * 获取context对象
     *
     * @return
     */
    public static WalletApplication getApp() {
        return app;
    }

    public static Locale getSetLocale(Context base) {
        // 获取应用内记录的用户语言选择
        String appLanguage = PreferencesUtils.getString(base, "appLanguage", Constant.LANGUAGE_DEFAULT);
        // 返回对应的系统语言
        if (appLanguage.equals(Constant.LANGUAGE_ARRAY[0])) {
            LogUtils.log("当前应用语言是 - 中文");
            lang = ""; // 默认是中文
            return Locale.CHINESE;//设置APP语言设置为中文
        } else if (appLanguage.equals(Constant.LANGUAGE_ARRAY[1])) {
            LogUtils.log("当前应用语言是 - 英文");
            lang = Constant.LANG_ENGLISH;
            return Locale.ENGLISH;//设置APP语言设置为英文
        } else {
            LogUtils.log("未知语种，默认语言 - 中文");
            return Locale.CHINESE;
        }
    }

    @Override
    public void onTrimMemory(int level) {
        super.onTrimMemory(level);
        if (level == TRIM_MEMORY_UI_HIDDEN) {
            ImageLoader.getInstance().clearMemoryCache();
        }
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        ImageLoader.getInstance().clearMemoryCache();
    }

    private void initImageLoader(Context context) {
        ImageLoaderConfiguration config = new ImageLoaderConfiguration
                .Builder(context)
//                .memoryCacheExtraOptions(480, 800) // max width, max height，即保存的每个缓存文件的最大长宽
//                .discCacheExtraOptions(480, 800, CompressFormat.JPEG, 75, null) // Can slow ImageLoader, use it carefully (Better don't use it)/设置缓存的详细信息，最好不要设置这个
                .threadPoolSize(5)//线程池内加载的数量
                .threadPriority(Thread.NORM_PRIORITY - 2)
                .denyCacheImageMultipleSizesInMemory()
                .memoryCache(new UsingFreqLimitedMemoryCache(100 * 1024 * 1024)) // You can pass your own memory cache implementation/你可以通过自己的内存缓存实现
                .memoryCacheSize(100 * 1024 * 1024)
                .discCacheSize(50 * 1024 * 1024)
//                .discCacheFileNameGenerator(new Md5FileNameGenerator())//将保存的时候的URI名称用MD5 加密
                .tasksProcessingOrder(QueueProcessingType.LIFO)
//                .discCacheFileCount(100) //缓存的文件数量
//                .discCache(new UnlimitedDiscCache(cacheDir))//自定义缓存路径
                .defaultDisplayImageOptions(DisplayImageOptions.createSimple())
                .imageDownloader(new BaseImageDownloader(context, 30 * 1000, 60 * 1000)) // connectTimeout (5 s), readTimeout (30 s)超时时间
//                .writeDebugLogs() // Remove for release app
                .build();//开始构建
        // Initialize ImageLoader with configuration.

        ImageLoader.getInstance().init(config);//全局初始化此配置
    }



}
