package com.tianji.blockchain.fragment.wallet;

import android.content.Context;

import com.android.volley.VolleyError;
import com.tianji.blockchainwallet.entity.WalletInfo;
import com.tianji.blockchain.Constant;
import com.tianji.blockchain.WalletApplication;
import com.tianji.blockchain.basic.BasicMvpInterface;
import com.tianji.blockchain.basic.BasicPresenter;
import com.tianji.blockchain.entity.AssetsDetailsItemEntity;
import com.tianji.blockchain.entity.InformationListEntity;
import com.tianji.blockchain.entity.WalletFragmentAssetsListEntity;
import com.tianji.blockchain.sharepreferences.InformationListSharedPreferences;
import com.tianji.blockchain.utils.HttpVolley;
import com.tianji.blockchain.utils.LogUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class WalletFragmentPresenter extends BasicPresenter {
    public static final int TYPE_CHECK_ETH_BALANCE = 0;
    public static final int TYPE_GET_NONCE = 1;
    public static final int TYPE_GET_GASPRICE = 2;
    public static final int TYPE_TRANSFER = 3;
    public static final int TYPE_CHECK_ERC20_BALANCE = 4;
    public static final int TYPE_GET_TRANSFER_INFO = 5;
    public static final int TYPE_GET_ASSETS_INFO_LIST = 6;
    public static final int TYPE_GET_INFORMATION_LIST = 7;

    public WalletFragmentPresenter(Context context, BasicMvpInterface basicMvpInterface) {
        super(context, basicMvpInterface);
    }

    @Override
    public void getData(Map<String, String> params) {
        String url = Constant.HttpUrl.checkETHBalance + params.get("address");
        HttpVolley.getInstance(context).HttpVolleyGet(url, new HttpVolley.VolleyCallBack() {
            @Override
            public void onSuccess(String data) {
                if (basicMvpInterface != null) {
                    basicMvpInterface.getDataSuccess(data, TYPE_CHECK_ETH_BALANCE);
                }
            }

            @Override
            public void onFail(VolleyError error) {
                LogUtils.log("请求eth余额错误" + error);
                basicMvpInterface.getDataFail(error, TYPE_CHECK_ETH_BALANCE);
            }
        });
    }

    public void checkERC20Blance(String address, String contractAddress) {
        String url = Constant.HttpUrl.checkERC20Balance + address + "&contractAddress=" + contractAddress;
        LogUtils.log("请求地址checkERC20Blance=" + url);
        HttpVolley.getInstance(context).HttpVolleyGet(url, new HttpVolley.VolleyCallBack() {
            @Override
            public void onSuccess(String data) {
                LogUtils.log("请求erc20余额成功==" + data);
                basicMvpInterface.getDataSuccess(data, TYPE_CHECK_ERC20_BALANCE);
            }

            @Override
            public void onFail(VolleyError error) {
                LogUtils.log("请求erc20余额错误==" + error);
                basicMvpInterface.getDataFail(error, TYPE_CHECK_ERC20_BALANCE);
            }
        });
    }

    /***批量查询ETH,ERC20余额，价值***/
    public void checkAssetsList(WalletInfo walletInfo, List<AssetsDetailsItemEntity> assetsDetailsItemEntityList) {
        String contractAddress = "";
        WalletFragmentAssetsListEntity entity = new WalletFragmentAssetsListEntity();
        entity.setKey(walletInfo.getAddress() + walletInfo.getChain());
        List<AssetsDetailsItemEntity> assetsList = new ArrayList<>();
        assetsList.addAll(assetsDetailsItemEntityList);
        for (int i = 1; i < assetsDetailsItemEntityList.size(); i++) {
            if (i == assetsDetailsItemEntityList.size() - 1) {
                contractAddress += assetsDetailsItemEntityList.get(i).getContractAddress();
            } else {
                contractAddress += assetsDetailsItemEntityList.get(i).getContractAddress() + ",";
            }
        }
        LogUtils.log("请求首页资产列表的参数 == " + contractAddress);
        String url = null;
        if (WalletApplication.getApp().getCurrentCurrency() == Constant.CurrencyType.TYPE_CNY) {
            url = Constant.HttpUrl.ETH_GET_ASSETS_INFO_LIST + "address=" + walletInfo.getAddress() + "&contractAddresses=" + contractAddress + "&currencyType=0";
        } else if (WalletApplication.getApp().getCurrentCurrency() == Constant.CurrencyType.TYPE_USD) {
            url = Constant.HttpUrl.ETH_GET_ASSETS_INFO_LIST + "address=" + walletInfo.getAddress() + "&contractAddresses=" + contractAddress + "&currencyType=1";
        }

        HttpVolley.getInstance(context).HttpVolleyGet(url, new HttpVolley.VolleyCallBack() {
            @Override
            public void onSuccess(String data) {
                LogUtils.log("请求ETH/ERC20价值列表成功 == " + data);
                try {
                    JSONObject rootObj = new JSONObject(data);
                    Iterator iterator = rootObj.keys();
                    while (iterator.hasNext()) {
                        String key = (String) iterator.next();
                        JSONObject resultObj = rootObj.optJSONObject(key);
                        if (key.equals("ETH")) {
                            assetsList.get(0).setBalance(resultObj.optDouble("balance"));
                            assetsList.get(0).setUnitPrice(resultObj.optDouble("unitPrice"));
                            assetsList.get(0).setTotalPrice(resultObj.optDouble("totalPrice"));
                        }
                        for (int i = 1; i < assetsList.size(); i++) {
                            if (assetsList.get(i).getContractAddress().equals(key)) {
                                assetsList.get(i).setBalance(resultObj.optDouble("balance"));
                                assetsList.get(i).setUnitPrice(resultObj.optDouble("unitPrice"));
                                assetsList.get(i).setTotalPrice(resultObj.optDouble("totalPrice"));
                            }
                        }
                    }
                    entity.setAssetsList(assetsList);

                    basicMvpInterface.getDataSuccess(entity, TYPE_GET_ASSETS_INFO_LIST);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFail(VolleyError error) {
                basicMvpInterface.getDataFail(error, TYPE_GET_ASSETS_INFO_LIST);
            }
        });
    }

    /**
     * 获取消息列表集合
     *
     * @param params
     */
    public void getInformationList(Map<String, String> params) {
        String url = Constant.HttpUrl.getSystemMsgList + "page=" + params.get("page") + "&size=" + params.get("pageSize");
        HttpVolley.getInstance(context).HttpVolleyGet(url, new HttpVolley.VolleyCallBack() {
            @Override
            public void onSuccess(String data) {
                InformationListEntity entity = gson.fromJson(data, InformationListEntity.class);
                LogUtils.log("获取到的消息列表数据是" + entity.getContent());
                if (InformationListSharedPreferences.getInstance(context).getInformationList() == null) {
                    for (int i = 0; i < entity.getContent().size(); i++) {
                        entity.getContent().get(i).setReaded(false);
                    }
                    InformationListSharedPreferences.getInstance(context).addInformationList(entity);
                    basicMvpInterface.getDataSuccess(entity, TYPE_GET_INFORMATION_LIST);
                } else {
                    InformationListEntity spEntity = InformationListSharedPreferences.getInstance(context).getInformationList();
                    if (entity.getContent().size() > 0) {
                        //获取到现在消息总数大于缓存总数,将缓存阅读清空加入新集合中
                        for (int i = 0; i < spEntity.getContent().size(); i++) {
                            for (int j = 0; j < entity.getContent().size(); j++) {
                                if (entity.getContent().get(j).getId() == spEntity.getContent().get(i).getId()) {
                                    entity.getContent().get(j).setReaded(spEntity.getContent().get(i).isReaded());
                                }
                            }
                        }
                        InformationListSharedPreferences.getInstance(context).addInformationList(entity);
                        basicMvpInterface.getDataSuccess(entity, TYPE_GET_INFORMATION_LIST);
                    } else {
                        basicMvpInterface.getDataSuccess(entity, TYPE_GET_INFORMATION_LIST);
                    }

                }
            }

            @Override
            public void onFail(VolleyError error) {
                basicMvpInterface.getDataFail(error, TYPE_GET_INFORMATION_LIST);
            }
        });
    }
}
