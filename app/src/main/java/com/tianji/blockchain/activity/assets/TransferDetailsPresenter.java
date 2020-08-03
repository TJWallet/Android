package com.tianji.blockchain.activity.assets;

import android.content.Context;

import com.android.volley.VolleyError;
import com.tianji.blockchain.Constant;
import com.tianji.blockchain.WalletApplication;
import com.tianji.blockchain.activity.basic.BasicCustomPresenter;
import com.tianji.blockchain.basic.BasicMvpInterface;
import com.tianji.blockchain.basic.BasicPresenter;
import com.tianji.blockchain.utils.HttpVolley;
import com.tianji.blockchain.utils.LogUtils;

import java.util.Map;

public class TransferDetailsPresenter extends BasicCustomPresenter {
    /***查询货币估值***/
    public static final int TYPE_CHECK_ETH_VALUE = 0;
    public static final int TYPE_CHECK_BTC_VALUE = 1;
    public static final int TYPE_CHECK_ACL_VALUE = 2;

    public TransferDetailsPresenter(Context context, BasicMvpInterface basicMvpInterface) {
        super(context, basicMvpInterface);
    }


    /**
     * 查询ACL货币估值
     */
    public void checkACLCoinValue(Context context) {
        String url = null;
        if (WalletApplication.getApp().isAclTest()) {
            if (WalletApplication.getApp().getCurrentCurrency() == Constant.CurrencyType.TYPE_CNY) {
                url = Constant.HttpUrl.ACL_TEST_GET_COIN_VALUE + "?currencyType=0";
            } else if (WalletApplication.getApp().getCurrentCurrency() == Constant.CurrencyType.TYPE_USD) {
                url = Constant.HttpUrl.ACL_TEST_GET_COIN_VALUE + "?currencyType=1";
            }
        } else {
            if (WalletApplication.getApp().getCurrentCurrency() == Constant.CurrencyType.TYPE_CNY) {
                url = Constant.HttpUrl.ACL_GET_COIN_VALUE + "?currencyType=0";
            } else if (WalletApplication.getApp().getCurrentCurrency() == Constant.CurrencyType.TYPE_USD) {
                url = Constant.HttpUrl.ACL_GET_COIN_VALUE + "?currencyType=1";
            }
        }
        HttpVolley.getInstance(context).HttpVolleyGet(url, new HttpVolley.VolleyCallBack() {
            @Override
            public void onSuccess(String data) {
                LogUtils.log("查询ACL货币估值 = " + data);
                basicMvpInterface.getDataSuccess(data, TYPE_CHECK_ACL_VALUE);
            }

            @Override
            public void onFail(VolleyError error) {
                basicMvpInterface.getDataFail(error, TYPE_CHECK_ACL_VALUE);
            }
        });
    }


    /**
     * 查询BTC货币估值
     */
    public void checkBTCCoinValue(Context context) {
        String url = null;
        if (WalletApplication.getApp().getCurrentCurrency() == Constant.CurrencyType.TYPE_CNY) {
            url = Constant.HttpUrl.BTC_GET_COIN_VALUE + "?currencyType=0";
        } else if (WalletApplication.getApp().getCurrentCurrency() == Constant.CurrencyType.TYPE_USD) {
            url = Constant.HttpUrl.BTC_GET_COIN_VALUE + "?currencyType=1";
        }
        HttpVolley.getInstance(context).HttpVolleyGet(url, new HttpVolley.VolleyCallBack() {
            @Override
            public void onSuccess(String data) {
                LogUtils.log("查询BTC货币估值 = " + data);
                basicMvpInterface.getDataSuccess(data, TYPE_CHECK_BTC_VALUE);
            }

            @Override
            public void onFail(VolleyError error) {
                basicMvpInterface.getDataFail(error, TYPE_CHECK_BTC_VALUE);
            }
        });
    }

    /**
     * 查询ETH货币估值
     */
    public void checkETHCoinValue(Context context, String contractAddress) {
        String url = null;
        if (contractAddress.equals("")) {
            if (WalletApplication.getApp().getCurrentCurrency() == Constant.CurrencyType.TYPE_CNY) {
                url = Constant.HttpUrl.getCoinValue + "?currencyType=0";
            } else if (WalletApplication.getApp().getCurrentCurrency() == Constant.CurrencyType.TYPE_USD) {
                url = Constant.HttpUrl.getCoinValue + "?currencyType=1";
            }
        } else {
            if (WalletApplication.getApp().getCurrentCurrency() == Constant.CurrencyType.TYPE_CNY) {
                url = Constant.HttpUrl.getCoinValue + "?contractAddress=" + contractAddress + "&currencyType=0";
            } else if (WalletApplication.getApp().getCurrentCurrency() == Constant.CurrencyType.TYPE_USD) {
                url = Constant.HttpUrl.getCoinValue + "?contractAddress=" + contractAddress + "&currencyType=1";
            }
        }
        HttpVolley.getInstance(context).HttpVolleyGet(url, new HttpVolley.VolleyCallBack() {
            @Override
            public void onSuccess(String data) {
                LogUtils.log("查询ETH货币估值 = " + data);
                basicMvpInterface.getDataSuccess(data, TYPE_CHECK_ETH_VALUE);
            }

            @Override
            public void onFail(VolleyError error) {
                basicMvpInterface.getDataFail(error, TYPE_CHECK_ETH_VALUE);
            }
        });
    }
}
