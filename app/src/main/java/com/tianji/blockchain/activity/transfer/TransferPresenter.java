package com.tianji.blockchain.activity.transfer;

import android.content.Context;


import com.android.volley.VolleyError;
import com.tianji.blockchain.Constant;
import com.tianji.blockchain.activity.basic.BasicCustomPresenter;
import com.tianji.blockchain.basic.BasicMvpInterface;
import com.tianji.blockchain.utils.HttpVolley;
import com.tianji.blockchain.utils.LogUtils;

import java.util.Map;


public class TransferPresenter extends BasicCustomPresenter {
    public static final int CHECK_FILECOIN_BALANCE = 0;
    public static final int CHECK_FILECOIN_NONCE = 1;
    public static final int FILECOIN_START_TRANSFER = 2;
    public static final int CHECK_FILECOIN_GASPRICE = 3;
    public static final int CHECK_FILECOIN_GASLIMIT = 4;


    public TransferPresenter(Context context, BasicMvpInterface basicMvpInterface) {
        super(context, basicMvpInterface);
    }

    /***请求获取FILECOIN余额***/
    public void checkFilecoinBalance(String walletAddress) {
        String url = Constant.HttpUrl.CHECK_FILECOIN_BALANCE + walletAddress;
        HttpVolley.getInstance(context).HttpVolleyGet(url, new HttpVolley.VolleyCallBack() {
            @Override
            public void onSuccess(String data) {
                LogUtils.log("查询filecoin余额成功 == " + data);
                basicMvpInterface.getDataSuccess(data, CHECK_FILECOIN_BALANCE);
            }

            @Override
            public void onFail(VolleyError error) {
                LogUtils.log("查询filecoin余额失败 == " + error.networkResponse.statusCode);
                basicMvpInterface.getDataFail(error, CHECK_FILECOIN_BALANCE);
            }
        });
    }

    /***请求FILECOIN NONCE***/
    public void checkFilecoinNonce(String walletAddress) {
        String url = Constant.HttpUrl.CHECK_FILECOIN_NONCE + walletAddress;
        HttpVolley.getInstance(context).HttpVolleyGet(url, new HttpVolley.VolleyCallBack() {
            @Override
            public void onSuccess(String data) {
                LogUtils.log("查询filecoin nonce成功 == " + data);
                basicMvpInterface.getDataSuccess(data, CHECK_FILECOIN_NONCE);
            }

            @Override
            public void onFail(VolleyError error) {
                LogUtils.log("查询filecoin nonce失败 == " + error.networkResponse.statusCode);
                basicMvpInterface.getDataFail(error, CHECK_FILECOIN_NONCE);
            }
        });
    }

    /***请求FILECOIN gasPrice***/
    public void checkFilecoinGasprice() {
        HttpVolley.getInstance(context).HttpVolleyGet(Constant.HttpUrl.CHECK_FILECOIN_GASPRICE, new HttpVolley.VolleyCallBack() {
            @Override
            public void onSuccess(String data) {
                LogUtils.log("查询filecoin gasprice成功 == " + data);
                basicMvpInterface.getDataSuccess(data, CHECK_FILECOIN_GASPRICE);
            }

            @Override
            public void onFail(VolleyError error) {
                LogUtils.log("查询filecoin gasprice失败 == " + error.networkResponse.statusCode);
                basicMvpInterface.getDataFail(error, CHECK_FILECOIN_GASPRICE);
            }
        });
    }

    /***请求FILECOIN gasLimit***/
    public void checkFilecoinGaslimit(String from, int nonce, String to, String value) {
        LogUtils.log("请求gaslimit参数 from=" + from + "||| nonce =" + nonce + "|||to==" + to + "|||value=" + value);
        String url = Constant.HttpUrl.CHECK_FILECOIN_GASLIMIT + "from=" + from + "&nonce=" + nonce + "&to=" + to + "&value=" + value;
        HttpVolley.getInstance(context).HttpVolleyGet(url, new HttpVolley.VolleyCallBack() {
            @Override
            public void onSuccess(String data) {
                LogUtils.log("查询filecoin gasLimit成功 == " + data);
                basicMvpInterface.getDataSuccess(data, CHECK_FILECOIN_GASLIMIT);
            }

            @Override
            public void onFail(VolleyError error) {
                LogUtils.log("查询filecoin gasLimit失败 == " + error.networkResponse.statusCode);
                basicMvpInterface.getDataFail(error, CHECK_FILECOIN_GASLIMIT);
            }
        });
    }

    /***请求FILECOIN 交易***/
    public void filecoinTransfer(Map<String, String> params) {
        HttpVolley.getInstance(context).HttpVolleyPost(Constant.HttpUrl.FILECOIN_START_TRANSFER, params, new HttpVolley.VolleyCallBack() {
            @Override
            public void onSuccess(String data) {
                LogUtils.log("filecoin 发起交易成功 == " + data);
                basicMvpInterface.getDataSuccess(data, FILECOIN_START_TRANSFER);
            }

            @Override
            public void onFail(VolleyError error) {
                LogUtils.log("filecoin 发起交易失败 == " + error.networkResponse.statusCode);
                basicMvpInterface.getDataFail(error, FILECOIN_START_TRANSFER);
            }
        });
    }


}
