package com.tianji.blockchain.activity.assets;

import android.content.Context;

import com.android.volley.VolleyError;
import com.google.gson.reflect.TypeToken;
import com.tianji.blockchain.Constant;
import com.tianji.blockchain.WalletApplication;
import com.tianji.blockchain.basic.BasicMvpInterface;
import com.tianji.blockchain.basic.BasicPresenter;
import com.tianji.blockchain.btcApi.BitcoinApi;
import com.tianji.blockchain.btcApi.Page;
import com.tianji.blockchain.btcApi.Transaction;
import com.tianji.blockchain.entity.TransferRecode;
import com.tianji.blockchain.utils.HttpVolley;
import com.tianji.blockchain.utils.LogUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.security.PublicKey;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class AssetsDetailsPresenter extends BasicPresenter {
    public static final int TYPE_GET_ETH_TRANSFER_RECODE = 0;
    public static final int TYPE_GET_ERC20_TRANSFER_RECODE = 1;
    public static final int TYPE_GET_VALUE = 2;
    public static final int TYPE_GET_ETH_BLANCE = 3;
    public static final int TYPE_GET_ERC20_BLANCE = 4;

    /***ACL***/
    public static final int TYPE_CHECK_ACL_BALANCE = 9;
    public static final int TYPE_GET_ACL_TRANSFER_RECODE = 10;
    public static final int TYPE_GET_ACL_VALUE = 13;

    /***BTC***/
    public static final int TYPE_CHECK_BTC_BALANCE = 11;
    public static final int TYPE_GET_BTC_TRANSFER_RECODE = 12;
    public static final int TYPE_GET_BTC_VALUE = 14;

    public AssetsDetailsPresenter(Context context, BasicMvpInterface basicMvpInterface) {
        super(context, basicMvpInterface);
    }

    @Override
    public void getData(Map<String, String> params) {
        //获取ETH交易记录
        String url = Constant.HttpUrl.getETHTransferRecode + params.get("address") + "&pageNo=" + params.get("pageNo") + "&pageSize=" + params.get("pageSize");
        HttpVolley.getInstance(context).HttpVolleyGet(url, new HttpVolley.VolleyCallBack() {
            @Override
            public void onSuccess(String data) {
                List<TransferRecode> list = gson.fromJson(data, new TypeToken<List<TransferRecode>>() {
                }.getType());
                LogUtils.log("获取ETH交易记录成功" + data);
                basicMvpInterface.getDataSuccess(list, TYPE_GET_ETH_TRANSFER_RECODE);
            }

            @Override
            public void onFail(VolleyError error) {
                LogUtils.log("获取ETH交易记录失败" + error);
                basicMvpInterface.getDataFail(error, TYPE_GET_ETH_TRANSFER_RECODE);
            }
        });
    }

    /**
     * 获取ERC20交易记录
     *
     * @param params
     */
    public void getERCtransferRecode(Map<String, String> params) {
        String url = Constant.HttpUrl.getERC20TransferRecode + "address=" + params.get("address") + "&contractAddress=" + params.get("contractAddress") + "&pageNo=" + params.get("pageNo") + "&pageSize=" + params.get("pageSize");
        LogUtils.log("getERCtransferRecode请求地址是=" + url);
        HttpVolley.getInstance(context).HttpVolleyGet(url, new HttpVolley.VolleyCallBack() {
            @Override
            public void onSuccess(String data) {
                List<TransferRecode> list = gson.fromJson(data, new TypeToken<List<TransferRecode>>() {
                }.getType());
                LogUtils.log("获取Erc20交易记录成功" + data);
                basicMvpInterface.getDataSuccess(list, TYPE_GET_ETH_TRANSFER_RECODE);
            }

            @Override
            public void onFail(VolleyError error) {
                LogUtils.log("获取Erc20交易记录失败" + error);
                basicMvpInterface.getDataFail(error, TYPE_GET_ETH_TRANSFER_RECODE);
            }
        });
    }

    /**
     * 查询货币估值
     *
     * @param contractAddress
     */
    public void checkCoinValue(String contractAddress) {

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
                basicMvpInterface.getDataSuccess(data, TYPE_GET_VALUE);
            }

            @Override
            public void onFail(VolleyError error) {
                basicMvpInterface.getDataFail(error, TYPE_GET_VALUE);
            }
        });
    }

    /**
     * 请求ERC20余额
     *
     * @param address
     * @param contractAddress
     */
    public void checkERC20Blance(String address, String contractAddress) {
        String url = Constant.HttpUrl.checkERC20Balance + address + "&contractAddress=" + contractAddress;
        LogUtils.log("请求地址checkERC20Blance=" + url);
        HttpVolley.getInstance(context).HttpVolleyGet(url, new HttpVolley.VolleyCallBack() {
            @Override
            public void onSuccess(String data) {
                basicMvpInterface.getDataSuccess(data, TYPE_GET_ERC20_BLANCE);
            }

            @Override
            public void onFail(VolleyError error) {
                basicMvpInterface.getDataFail(error, TYPE_GET_ERC20_BLANCE);
            }
        });
    }

    /**
     * 请求ETH余额
     *
     * @param walletAddress
     */
    public void checkETHBlance(String walletAddress) {
        String url = Constant.HttpUrl.checkETHBalance + walletAddress;
        HttpVolley.getInstance(context).HttpVolleyGet(url, new HttpVolley.VolleyCallBack() {
            @Override
            public void onSuccess(String data) {
                basicMvpInterface.getDataSuccess(data, TYPE_GET_ETH_BLANCE);
            }

            @Override
            public void onFail(VolleyError error) {
                basicMvpInterface.getDataFail(error, TYPE_GET_ETH_BLANCE);
            }
        });
    }

    /**
     * 获取acl余额
     */
    public void checkACLBalance(String address) {
        String url = "";
        if (WalletApplication.getApp().isAclTest()) {
            url = Constant.HttpUrl.ACL_TEST_GET_BALANCE + "?address=" + address;
        } else {
            url = Constant.HttpUrl.ACL_GET_BALANCE + "?address=" + address;
        }
        HttpVolley.getInstance(context).HttpVolleyGet(url, new HttpVolley.VolleyCallBack() {
            @Override
            public void onSuccess(String data) {
                LogUtils.log(" 获取acl余额成功==" + data);
                basicMvpInterface.getDataSuccess(data, TYPE_CHECK_ACL_BALANCE);
            }

            @Override
            public void onFail(VolleyError error) {
                LogUtils.log(" 获取acl余额失败==" + error);
                basicMvpInterface.getDataFail(error, TYPE_CHECK_ACL_BALANCE);
            }
        });
    }

    /**
     * 获取ACL得交易记录
     *
     * @param params
     */
    public void getAclTransferRecode(Map<String, String> params) {
        LogUtils.log("ACL 请求参数 = " + params.toString());
        String url = "";
        if (WalletApplication.getApp().isAclTest()) {
            LogUtils.log("ACL测试服请求");
            url = Constant.HttpUrl.ACL_TEST_GET_TRANSFER_RECODE;
        } else {
            url = Constant.HttpUrl.ACL_GET_TRANSFER_RECODE;
        }
        HttpVolley.getInstance(context).HttpVolleyPostBody(url, params, new HttpVolley.VolleyCallBack() {
            @Override
            public void onSuccess(String data) {
                LogUtils.log("获取ACL交易记录成功 ==" + data);
                try {
                    List<TransferRecode> list = new ArrayList<>();
                    JSONObject rootObj = new JSONObject(data);
                    JSONObject resultObj = rootObj.optJSONObject("result");
                    if (resultObj != null) {
                        JSONArray array = resultObj.optJSONArray("records");
                        for (int i = 0; i < array.length(); i++) {
                            TransferRecode transferRecode = new TransferRecode();
                            JSONObject itemObj = array.optJSONObject(i);
                            transferRecode.setTimestamp(itemObj.optLong("createdContractCodeIndexedAt") / 1000);
                            transferRecode.setBlockHash(itemObj.optString("blockHash"));
                            transferRecode.setBlockNumber(itemObj.optString("blockNumber"));
                            transferRecode.setValue(itemObj.optString("value"));
                            transferRecode.setFrom(itemObj.optString("fromAddressHash").toLowerCase());
                            transferRecode.setTo(itemObj.optString("toAddressHash").toLowerCase());
                            transferRecode.setGasPrice(itemObj.optString("gasPrice"));
                            transferRecode.setGas(itemObj.optString("gas"));
                            transferRecode.setHash(itemObj.optString("hash"));
                            transferRecode.setCumulativeGasUsed(itemObj.optString("cumulativeGasUsed"));
                            transferRecode.setGasUsed(itemObj.optString("gasUsed"));

                            list.add(transferRecode);
                        }
                    }
                    basicMvpInterface.getDataSuccess(list, TYPE_GET_ACL_TRANSFER_RECODE);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFail(VolleyError error) {
                LogUtils.log("获取ACL交易记录失败 =" + error.getMessage());
                basicMvpInterface.getDataSuccess(error, TYPE_GET_ACL_TRANSFER_RECODE);
            }
        });
    }

    /**
     * 获取BTC余额
     *
     * @param address
     */
    public void checkBTCBalance(String address) {
        BitcoinApi.getAccountBalance(context, address, new BitcoinApi.GetAccountBalanceCallback() {
            @Override
            public void onGetBalanceSuccess(Double balance) {
                basicMvpInterface.getDataSuccess(balance, TYPE_CHECK_BTC_BALANCE);
            }

            @Override
            public void onGetBalanceFailed(Exception e) {
                basicMvpInterface.getDataFail(e, TYPE_CHECK_BTC_BALANCE);
            }
        });
    }

    /**
     * 获取BTC交易记录
     *
     * @param address
     */
    public void getBTCTransferRecode(String address) {
        BitcoinApi.getTxList(context, address, 0, new BitcoinApi.GetTxListCallback() {
            @Override
            public void onGetTxListSuccess(Page<Transaction> page) {
                LogUtils.log("获取BTC交易列表成功 = " + page.toString());

                List<TransferRecode> transferRecodeList = new ArrayList<>();

                for (int i = 0; i < page.getItems().size(); i++) {
                    TransferRecode transferRecode = new TransferRecode();
                    Transaction transaction = page.getItems().get(i);

                    transferRecode.setTimestamp(transaction.getBlockTime());
                    transferRecode.setBlockHash(transaction.getBlockHash());
                    transferRecode.setBlockNumber(transaction.getBlockHeight() + "");
                    transferRecode.setHash(transaction.getHash());
                    BigDecimal b = new BigDecimal(transaction.getFee()).multiply(new BigDecimal(Math.pow(10, 8)));
                    transferRecode.setBtcFee(b.longValue());

                    boolean isInto = true;
                    for (int j = 0; j < transaction.getInputs().size(); j++) {
                        Transaction.Input input = transaction.getInputs().get(j);
                        if (input.getPrevAdresses().equals(address)) {
                            transferRecode.setTransferInTo(false);
                            isInto = false;
                            transferRecode.setFrom(address);
                            break;
                        } else {
                            transferRecode.setTransferInTo(true);
                            isInto = true;
                            transferRecode.setFrom(input.getPrevAdresses());
                        }

//                        LogUtils.log("第" + i + "笔交易第" + j + "个input ==" + input);
//                        LogUtils.log("第" + i + "笔交易第" + j + "个input address ==" + input.getPrevAdresses());
//                        LogUtils.log("第" + i + "笔交易第" + j + "个input value ==" + input.getPrevValue());
                    }
                    transferRecode.setInputList(transaction.getInputs());
                    double value = 0;
                    if (isInto) {
                        //转入,输出列表中匹配到自己，找到自己的金额
                        for (int j = 0; j < transaction.getOutputs().size(); j++) {
                            Transaction.Output output = transaction.getOutputs().get(j);
                            transferRecode.setTo(address);
                            if (output.getAddresses()[0].equals(address)) {
                                value += output.getValue();
                            }
                        }
                    } else {
                        //转出，输出列表中除了自己，金额累加
                        for (int j = 0; j < transaction.getOutputs().size(); j++) {
                            Transaction.Output output = transaction.getOutputs().get(j);
                            if (j == 0) {
                                transferRecode.setTo(output.getAddresses()[0]);
                            }
                            if (!output.getAddresses()[0].equals(address)) {
                                value += output.getValue();
                            }
//                        LogUtils.log("第" + i + "笔交易第" + j + "个output ==" + output);
//                        LogUtils.log("第" + i + "笔交易第" + j + "个output ==  address" + output.getAddresses()[0]);
//                        LogUtils.log("第" + i + "笔交易第" + j + "个output ==  value" + output.getValue());
                        }
                    }

                    LogUtils.log("交易的VALUE == " + value);
                    transferRecode.setOutputList(transaction.getOutputs());
                    transferRecode.setValue(value + "");

                    transferRecodeList.add(transferRecode);
                }

                basicMvpInterface.getDataSuccess(transferRecodeList, TYPE_GET_BTC_TRANSFER_RECODE);
            }

            @Override
            public void onGetTxListFailed(Exception e) {
                LogUtils.log("获取BTC交易列表失败 = " + e.toString());
                basicMvpInterface.getDataFail(e.toString(), TYPE_GET_BTC_TRANSFER_RECODE);
            }
        });
    }

    /**
     * 查询BTC货币估值
     */
    public void checkBTCCoinValue() {
        String url = null;
        if (WalletApplication.getApp().getCurrentCurrency() == Constant.CurrencyType.TYPE_CNY) {
            url = Constant.HttpUrl.BTC_GET_COIN_VALUE + "?currencyType=0";
        } else if (WalletApplication.getApp().getCurrentCurrency() == Constant.CurrencyType.TYPE_USD) {
            url = Constant.HttpUrl.BTC_GET_COIN_VALUE + "?currencyType=1";
        }
        HttpVolley.getInstance(context).HttpVolleyGet(url, new HttpVolley.VolleyCallBack() {
            @Override
            public void onSuccess(String data) {
                basicMvpInterface.getDataSuccess(data, TYPE_GET_BTC_VALUE);
            }

            @Override
            public void onFail(VolleyError error) {
                basicMvpInterface.getDataFail(error, TYPE_GET_BTC_VALUE);
            }
        });
    }

    /**
     * 查询ACL货币估值
     */
    public void checkACLCoinValue() {
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
                basicMvpInterface.getDataSuccess(data, TYPE_GET_ACL_VALUE);
            }

            @Override
            public void onFail(VolleyError error) {
                basicMvpInterface.getDataFail(error, TYPE_GET_ACL_VALUE);
            }
        });
    }


}
