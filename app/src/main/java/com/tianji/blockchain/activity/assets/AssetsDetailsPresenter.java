package com.tianji.blockchain.activity.assets;

import android.content.Context;

import com.android.volley.VolleyError;
import com.tianji.blockchain.Constant;
import com.tianji.blockchain.WalletApplication;
import com.tianji.blockchain.activity.basic.BasicCustomPresenter;
import com.tianji.blockchain.basic.BasicMvpInterface;
import com.tianji.blockchain.entity.TransferRecode;
import com.tianji.blockchain.utils.HttpVolley;
import com.tianji.blockchain.utils.LogUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class AssetsDetailsPresenter extends BasicCustomPresenter {
    public static final int CHECK_TRANSFER_RECORD = 0;
    public static final int CHECK_FILECOIN_BALANCE = 1;
    public static final int CHECK_FILECOIN_VALUE = 2;


    public AssetsDetailsPresenter(Context context, BasicMvpInterface basicMvpInterface) {
        super(context, basicMvpInterface);
    }

    public void checkTransferRecord(String walletAddress) {
        String url = Constant.HttpUrl.CHECK_FILECOIN_TRANSFER_RECORD + "address=" + walletAddress + "&page=0&size=500&sort=Timestamp,desc";
        HttpVolley.getInstance(context).HttpVolleyGet(url, new HttpVolley.VolleyCallBack() {
            @Override
            public void onSuccess(String data) {
                LogUtils.log("查询filecoin 交易记录成功 == " + data);
                try {
                    List<TransferRecode> list = new ArrayList<>();
                    JSONObject rootObj = new JSONObject(data);
                    JSONArray array = rootObj.optJSONArray("content");
                    for (int i = 0; i < array.length(); i++) {
                        TransferRecode transferRecode = new TransferRecode();
                        JSONObject itemObj = array.optJSONObject(i);
                        transferRecode.setTimestamp(itemObj.optLong("timestamp"));
                        transferRecode.setBlockHash(itemObj.optString("blockCid"));
                        transferRecode.setBlockNumber(itemObj.optString("blockHeight"));
                        transferRecode.setValue(itemObj.optString("value"));
                        transferRecode.setFrom(itemObj.optString("from").toLowerCase());
                        transferRecode.setTo(itemObj.optString("to").toLowerCase());
                        transferRecode.setGasPrice(itemObj.optString("gasPrice"));
                        transferRecode.setGas(itemObj.optString("gasLimit"));
                        transferRecode.setHash(itemObj.optString("id"));

                        list.add(transferRecode);
                    }
                    basicMvpInterface.getDataSuccess(list, CHECK_TRANSFER_RECORD);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFail(VolleyError error) {
                LogUtils.log("查询filecoin 交易记录失败 == " + error.networkResponse.statusCode);
                basicMvpInterface.getDataFail(error, CHECK_TRANSFER_RECORD);
            }
        });
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

    /**
     * 查询FIL货币估值
     */
    public void checkFILValue() {
        String url = null;
        if (WalletApplication.getApp().getCurrentCurrency() == Constant.CurrencyType.TYPE_CNY) {
            url = Constant.HttpUrl.CHECK_FILECOIN_VALUE + "?currencyType=0";
        } else if (WalletApplication.getApp().getCurrentCurrency() == Constant.CurrencyType.TYPE_USD) {
            url = Constant.HttpUrl.CHECK_FILECOIN_VALUE + "?currencyType=1";
        }
        HttpVolley.getInstance(context).HttpVolleyGet(url, new HttpVolley.VolleyCallBack() {
            @Override
            public void onSuccess(String data) {
                basicMvpInterface.getDataSuccess(data, CHECK_FILECOIN_VALUE);
            }

            @Override
            public void onFail(VolleyError error) {
                basicMvpInterface.getDataFail(error, CHECK_FILECOIN_VALUE);
            }
        });
    }
}
