package com.tianji.blockchain.activity.transfer;

import android.content.Context;

import com.android.volley.VolleyError;
import com.tianji.blockchain.Constant;
import com.tianji.blockchain.WalletApplication;
import com.tianji.blockchain.basic.BasicMvpInterface;
import com.tianji.blockchain.basic.BasicPresenter;
import com.tianji.blockchain.entity.AclTransferInfoEntity;
import com.tianji.blockchain.entity.TransferResuletInfoEntity;
import com.tianji.blockchain.utils.HttpVolley;
import com.tianji.blockchain.utils.LogUtils;

import org.bitcoinj.core.UTXO;

import java.util.List;
import java.util.Map;

public class TransferPresenter extends BasicPresenter {
    /***ETH&ERC20***/
    public static final int TYPE_CHECK_ETH_BALANCE = 0;
    public static final int TYPE_GET_ETH_BALANCE = 14;
    public static final int TYPE_GET_ETH_NONCE = 1;
    public static final int TYPE_GET_ETH_GASPRICE = 2;
    public static final int TYPE_ETH_TRANSFER = 3;
    public static final int TYPE_CHECK_ERC20_BALANCE = 4;
    public static final int TYPE_GET_ETH_TRANSFER_INFO = 5;
    /***ACL***/
    public static final int TYPE_GET_ACL_NONCE = 6;
    public static final int TYPE_GET_ACL_GASPRICE = 7;
    public static final int TYPE_ACL_TRANSFER = 8;
    public static final int TYPE_CHECK_ACL_BALANCE = 9;
    public static final int TYPE_CHECK_ACL_TRANSFER_INFO = 15;
    /***BTC***/
    public static final int TYPE_GET_BTC_FEE = 10;
    public static final int TYPE_CHECK_BTC_BALANCE = 11;
    public static final int TYPE_GET_BTC_UTXO = 12;
    public static final int TYPE_BTC_TRANSFER = 13;

    /***查询货币估值***/
    public static final int TYPE_CHECK_ETH_VALUE = 16;
    public static final int TYPE_CHECK_BTC_VALUE = 17;
    public static final int TYPE_CHECK_ACL_VALUE = 18;
    public static final int TYPE_CHECK_ERC20_VALUE = 19;

    public TransferPresenter(Context context, BasicMvpInterface basicMvpInterface) {
        super(context, basicMvpInterface);
    }

    /**
     * 查询ETH余额
     *
     * @param params
     */
    @Override
    public void getData(Map<String, String> params) {
        String url = Constant.HttpUrl.checkETHBalance + params.get("address");
        HttpVolley.getInstance(context).HttpVolleyGet(url, new HttpVolley.VolleyCallBack() {
            @Override
            public void onSuccess(String data) {
                basicMvpInterface.getDataSuccess(data, TYPE_CHECK_ETH_BALANCE);
            }

            @Override
            public void onFail(VolleyError error) {
                LogUtils.log("请求eth余额错误" + error);
            }
        });
    }

    public void checkERC20Blance(String address, String contractAddress) {
        String url = Constant.HttpUrl.checkERC20Balance + address + "&contractAddress=" + contractAddress;
        LogUtils.log("TransferPresenter checkERC20Blance=" + url);
        HttpVolley.getInstance(context).HttpVolleyGet(url, new HttpVolley.VolleyCallBack() {
            @Override
            public void onSuccess(String data) {
                LogUtils.log("TransferPresenter请求erc20余额成功==" + data);
                basicMvpInterface.getDataSuccess(data, TYPE_CHECK_ERC20_BALANCE);
            }

            @Override
            public void onFail(VolleyError error) {
                LogUtils.log("TransferPresenter请求erc20余额错误==" + error);
            }
        });
    }

    /**
     * 查询ETH余额，用于ERC20对比矿工费用
     *
     * @param address
     */
    public void getEthBlance(String address) {
        String url = Constant.HttpUrl.checkETHBalance + address;
        HttpVolley.getInstance(context).HttpVolleyGet(url, new HttpVolley.VolleyCallBack() {
            @Override
            public void onSuccess(String data) {
                basicMvpInterface.getDataSuccess(data, TYPE_GET_ETH_BALANCE);
            }

            @Override
            public void onFail(VolleyError error) {
                LogUtils.log("请求eth余额错误" + error);
            }
        });
    }

    /**
     * 获取NONCE
     *
     * @param address
     */
    public void getEHTNonce(String address) {
        String url = Constant.HttpUrl.getNonce + address;
        HttpVolley.getInstance(context).HttpVolleyGet(url, new HttpVolley.VolleyCallBack() {
            @Override
            public void onSuccess(String data) {
                LogUtils.log("请求nonce成功==" + data);
                basicMvpInterface.getDataSuccess(data, TYPE_GET_ETH_NONCE);
            }

            @Override
            public void onFail(VolleyError error) {
                LogUtils.log("请求nonce失败==" + error);
            }
        });
    }

    /**
     * 获取gasprice
     */
    public void getETHGasPrice() {
        HttpVolley.getInstance(context).HttpVolleyGet(Constant.HttpUrl.getGasPrice, new HttpVolley.VolleyCallBack() {
            @Override
            public void onSuccess(String data) {
                LogUtils.log("请求gasprice成功==" + data);
                basicMvpInterface.getDataSuccess(data, TYPE_GET_ETH_GASPRICE);
            }

            @Override
            public void onFail(VolleyError error) {
                LogUtils.log("请求gasprice失败==" + error);
            }
        });
    }

    /**
     * 发起ETH交易
     *
     * @param params
     */
    public void ethTransfer(Map<String, String> params) {
        LogUtils.log("转账交易的URL == " + Constant.HttpUrl.transferUrl);
        HttpVolley.getInstance(context).HttpVolleyPost(Constant.HttpUrl.transferUrl, params, new HttpVolley.VolleyCallBack() {
            @Override
            public void onSuccess(String data) {
                LogUtils.log("交易请求成功" + data);
                basicMvpInterface.getDataSuccess(data, TYPE_ETH_TRANSFER);
            }

            @Override
            public void onFail(VolleyError error) {
                LogUtils.log("交易请求失败" + error);
            }
        });
    }

    /**
     * 根据交易HASH获取ETH交易详情
     *
     * @param hash
     */
    public void getETHTransferInfo(String hash) {
        String url = Constant.HttpUrl.getTransferInfo + hash;
        LogUtils.log("URL == " + url);
        HttpVolley.getInstance(context).HttpVolleyGet(url, new HttpVolley.VolleyCallBack() {
            @Override
            public void onSuccess(String data) {
                LogUtils.log("获取交易详情成功==" + data);
                TransferResuletInfoEntity entity = gson.fromJson(data, TransferResuletInfoEntity.class);
                LogUtils.log("GSON解析成功== " + entity.getBlockNumber() + entity.getHash());
                basicMvpInterface.getDataSuccess(entity, TYPE_GET_ETH_TRANSFER_INFO);
            }

            @Override
            public void onFail(VolleyError error) {
                LogUtils.log("获取交易详情失败==" + error);
            }
        });
    }

    /**
     * 获取acl GasPrice
     */
    public void getACLGasPrice() {
        String url = "";
        if (WalletApplication.getApp().isAclTest()) {
            url = Constant.HttpUrl.ACL_TEST_GET_GASPRICE;
        } else {
            url = Constant.HttpUrl.ACL_GET_GASPRICE;
        }
        HttpVolley.getInstance(context).HttpVolleyGet(url, new HttpVolley.VolleyCallBack() {
            @Override
            public void onSuccess(String data) {
                LogUtils.log("获取acl gasprice成功  gasprice=" + data);
                basicMvpInterface.getDataSuccess(data, TYPE_GET_ACL_GASPRICE);
            }

            @Override
            public void onFail(VolleyError error) {
                LogUtils.log("获取acl gasprice失败 =" + error);
                basicMvpInterface.getDataFail(error, TYPE_GET_ACL_GASPRICE);
            }
        });
    }

    public void getACLTransferInfo(String hash) {
        String url = "";
        if (WalletApplication.getApp().isAclTest()) {
            url = Constant.HttpUrl.ACL_TEST_GET_TRANSFER_INFO + hash;
        } else {
            url = Constant.HttpUrl.ACL_GET_TRANSFER_INFO + hash;
        }
        LogUtils.log("URL == " + url);
        HttpVolley.getInstance(context).HttpVolleyGet(url, new HttpVolley.VolleyCallBack() {
            @Override
            public void onSuccess(String data) {
                LogUtils.log("获取ACL交易详情成功==" + data);
                AclTransferInfoEntity entity = gson.fromJson(data, AclTransferInfoEntity.class);
//                LogUtils.log("GSON解析成功== " + entity.getBlockNumber() + entity.getHash());
                basicMvpInterface.getDataSuccess(entity, TYPE_CHECK_ACL_TRANSFER_INFO);
            }

            @Override
            public void onFail(VolleyError error) {
                basicMvpInterface.getDataFail(error, TYPE_CHECK_ACL_TRANSFER_INFO);
                LogUtils.log("获取ACL交易详情失败==" + error);
            }
        });
    }

    /**
     * 获取acl nonce
     */
    public void getACLNonce(String address) {
        String url = "";
        if (WalletApplication.getApp().isAclTest()) {
            url = Constant.HttpUrl.ACL_TEST_GET_NONCE + "?address=" + address;
        } else {
            url = Constant.HttpUrl.ACL_GET_NONCE + "?address=" + address;
        }

        HttpVolley.getInstance(context).HttpVolleyGet(url, new HttpVolley.VolleyCallBack() {
            @Override
            public void onSuccess(String data) {
                LogUtils.log("获取acl nonce成功  nonce=" + data);
                basicMvpInterface.getDataSuccess(data, TYPE_GET_ACL_NONCE);
            }

            @Override
            public void onFail(VolleyError error) {
                LogUtils.log("获取acl nonce失败 =" + error);
                basicMvpInterface.getDataFail(error, TYPE_GET_ACL_NONCE);
            }
        });
    }

    /**
     * acl交易
     */
    public void aclTransfer(Map<String, String> params) {
        String url = "";
        if (WalletApplication.getApp().isAclTest()) {
            url = Constant.HttpUrl.ACL_TEST_TRANSACTION;
        } else {
            url = Constant.HttpUrl.ACL_TRANSACTION;
        }
        HttpVolley.getInstance(context).HttpVolleyPost(url, params, new HttpVolley.VolleyCallBack() {
            @Override
            public void onSuccess(String data) {
                LogUtils.log("acl交易成功 hash =" + data);
                basicMvpInterface.getDataSuccess(data, TYPE_ACL_TRANSFER);
            }

            @Override
            public void onFail(VolleyError error) {
                LogUtils.log("acl交易失败 =" + error);
                basicMvpInterface.getDataFail(error, TYPE_ACL_TRANSFER);
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
     * 获取BTC的矿工费用
     */
    public void getBTCGasPrice() {
        BitcoinApi.getGasPrice(context, new BitcoinApi.GetRecommandGasPriceCallback() {
            @Override
            public void onGetGasPriceSuccess(RecommandGasPrice gasPrice) {
                LogUtils.log(" 获取BTC gasPrice 成功==" + gasPrice.toString());
                basicMvpInterface.getDataSuccess(gasPrice, TYPE_GET_BTC_FEE);
            }

            @Override
            public void onGetGasPriceFailed(Exception e) {
                LogUtils.log(" 获取BTC gasPrice 失败==" + e.toString());
                basicMvpInterface.getDataFail(e, TYPE_GET_BTC_FEE);
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
     * 获取BTC的未花费列表
     *
     * @param address
     */
    public void getBTCUtxo(String address) {
        BitcoinApi.getUtxo(context, address, new BitcoinApi.GetUtxoCallback() {
            @Override
            public void onGetUtxoSuccess(List<UTXO> utxos) {
                for (int i = 0; i < utxos.size(); i++) {
                    LogUtils.log(i + " -- value == " + utxos.get(i).getValue());
                }
                basicMvpInterface.getDataSuccess(utxos, TYPE_GET_BTC_UTXO);
            }

            @Override
            public void onGetUtxoFailed(Exception e) {
                basicMvpInterface.getDataFail(e, TYPE_CHECK_BTC_BALANCE);
            }
        });
    }

    /**
     * 发起BTC交易
     */
    public void btcTransfer(Map<String, String> params) {
//        BitcoinApi.sendRawTransaction(sign, new BitcoinApi.SendRawTransactionCallback() {
//            @Override
//            public void onSendRawTransactionSuccess(String hash) {
//                LogUtils.log("btc发起交易成功 =" + hash);
//                basicMvpInterface.getDataSuccess(hash, TYPE_BTC_TRANSFER);
//            }
//
//            @Override
//            public void onSendRawTransactionFailed(Exception e) {
//                LogUtils.log("btc发起交易失败 =" + e.toString());
//                basicMvpInterface.getDataFail(e.toString(), TYPE_BTC_TRANSFER);
//            }
//        });
        String url = Constant.HttpUrl.BTC_TRANSFER;
        HttpVolley.getInstance(context).HttpVolleyPost(url, params, new HttpVolley.VolleyCallBack() {
            @Override
            public void onSuccess(String data) {
                LogUtils.log("btc发起交易成功 =" + data);
                basicMvpInterface.getDataSuccess(data, TYPE_BTC_TRANSFER);
            }

            @Override
            public void onFail(VolleyError error) {
                LogUtils.log("btc发起交易失败 =" + error.toString());
                basicMvpInterface.getDataFail(error, TYPE_BTC_TRANSFER);
            }
        });
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
    public void checkETHCoinValue(Context context) {
        String url = null;
        if (WalletApplication.getApp().getCurrentCurrency() == Constant.CurrencyType.TYPE_CNY) {
            url = Constant.HttpUrl.getCoinValue + "?currencyType=0";
        } else if (WalletApplication.getApp().getCurrentCurrency() == Constant.CurrencyType.TYPE_USD) {
            url = Constant.HttpUrl.getCoinValue + "?currencyType=1";
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

    /**
     * 获取ERC20货币价值
     *
     * @param context
     * @param contractAddress
     */
    public void checkErc20CoinValue(Context context, String contractAddress) {
        String url = null;
        if (WalletApplication.getApp().getCurrentCurrency() == Constant.CurrencyType.TYPE_CNY) {
            url = Constant.HttpUrl.getCoinValue + "?contractAddress=" + contractAddress + "&currencyType=0";
        } else if (WalletApplication.getApp().getCurrentCurrency() == Constant.CurrencyType.TYPE_USD) {
            url = Constant.HttpUrl.getCoinValue + "?contractAddress=" + contractAddress + "&currencyType=1";
        }

        HttpVolley.getInstance(context).HttpVolleyGet(url, new HttpVolley.VolleyCallBack() {
            @Override
            public void onSuccess(String data) {
                LogUtils.log("查询ERC20货币估值 = " + data);
                basicMvpInterface.getDataSuccess(data, TYPE_CHECK_ERC20_VALUE);
            }

            @Override
            public void onFail(VolleyError error) {
                basicMvpInterface.getDataFail(error, TYPE_CHECK_ERC20_VALUE);
            }
        });
    }


}
