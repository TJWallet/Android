package com.tianji.blockchain.sharepreferences;

import android.content.Context;
import android.content.SharedPreferences;

import com.tianji.blockchain.Constant;

/***通用缓存类,用于缓存一些简单的数据格式***/
public class CommonSharedPreferences {
    public static final String COMMON_SP = "COMMON_SP";
    /***用于记录是否是开发者模式***/
    public static final String IS_DEVELOPER_SP = "IS_DEVELOPER_SP";
    /***用于记录货币 1：人民币 2：美元***/
    public static final String CURRENCY_SP = "CURRENCY_SP";
    /***用于记录ACL是否是测试服环境***/
    public static final String ACL_TEST_SP = "ACL_TEST_SP";
    private Context context;
    private SharedPreferences.Editor mSharedPreferncesEditor;
    private SharedPreferences mSharedPrefernces;

    /***单例模式***/
    private static CommonSharedPreferences instance;

    private CommonSharedPreferences(Context context) {
        this.context = context;
        mSharedPrefernces = context.getSharedPreferences(COMMON_SP, Context.MODE_PRIVATE);
        mSharedPreferncesEditor = mSharedPrefernces.edit();
    }

    public static CommonSharedPreferences getInstance(Context context) {
        if (instance == null) {
            instance = new CommonSharedPreferences(context);
        }
        return instance;
    }

    /***更换是否是开发者模式的缓存***/
    public void changeDeveloperMode(boolean b) {
        mSharedPreferncesEditor.putBoolean(IS_DEVELOPER_SP, b).commit();
    }

    /***获取是否是开发者模式的缓存***/
    public boolean getDeveloperMode() {
        return mSharedPrefernces.getBoolean(IS_DEVELOPER_SP, false);
    }

    /***切换货币种类***/
    public void changeCurrency(int currency) {
        mSharedPreferncesEditor.putInt(CURRENCY_SP, currency).commit();
    }

    /***获取货币种类***/
    public int getCurrency() {
        return mSharedPrefernces.getInt(CURRENCY_SP, Constant.CurrencyType.TYPE_CNY);
    }

    /***切换ACL是否是测试服***/
    public void changeAclTest(boolean isTest) {
        mSharedPreferncesEditor.putBoolean(ACL_TEST_SP, isTest).commit();
    }

    /***获取ACL是否是测试服***/
    public boolean getAclTest() {
        return mSharedPrefernces.getBoolean(ACL_TEST_SP, false);
    }
}
