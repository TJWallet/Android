package com.tianji.blockchain.activity.transfer;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.tianji.blockchainwallet.WalletManager;
import com.tianji.blockchainwallet.constant.enums.Chain;
import com.tianji.blockchainwallet.constant.enums.ResultCode;
import com.tianji.blockchainwallet.constant.enums.StorageSaveType;
import com.tianji.blockchainwallet.constant.enums.TransferType;
import com.tianji.blockchainwallet.entity.WalletInfo;
import com.tianji.blockchainwallet.wallet.IRequestListener;
import com.tianji.blockchain.Constant;
import com.tianji.blockchain.IntentFilterConstant;
import com.tianji.blockchain.R;
import com.tianji.blockchain.WalletApplication;
import com.tianji.blockchain.activity.assets.SelectAssetsActivity;
import com.tianji.blockchain.activity.assets.TransferDetailsActivity;
import com.tianji.blockchain.activity.basic.BasicConnectShowActivity;
import com.tianji.blockchain.activity.basic.UsbCallbackListener;
import com.tianji.blockchain.activity.main.AddressBookActivity;
import com.tianji.blockchain.activity.sacn.MipcaActivityCapture;
import com.tianji.blockchain.basic.BasicMvpInterface;
import com.tianji.blockchain.broadcast.NetChangeListener;
import com.tianji.blockchain.btcApi.RecommandGasPrice;
import com.tianji.blockchain.dialog.PasswordDialog;
import com.tianji.blockchain.dialog.PinDialog;
import com.tianji.blockchain.dialog.TipsDialog;
import com.tianji.blockchain.dialog.TransferDetailsDialog;
import com.tianji.blockchain.entity.AclTransferInfoEntity;
import com.tianji.blockchain.entity.AssetsDetailsItemEntity;
import com.tianji.blockchain.entity.DialogEntity;
import com.tianji.blockchain.entity.TransferRecode;
import com.tianji.blockchain.entity.TransferWaitEntity;
import com.tianji.blockchain.sharepreferences.TransferWaitSharedPreferences;
import com.tianji.blockchain.usbInterfack.UsbComparSerialNumberCallbackListener;
import com.tianji.blockchain.utils.AddressUtils;
import com.tianji.blockchain.utils.CommonUtils;
import com.tianji.blockchain.utils.ImageLoaderHelper;
import com.tianji.blockchain.utils.LogUtils;
import com.tianji.blockchain.utils.MathUtils;
import com.tianji.blockchain.utils.NetUtils;
import com.tianji.blockchain.utils.ViewCommonUtils;
import com.tianji.blockchain.utils.WalletHelper;

import org.bitcoinj.core.UTXO;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TransferActivity extends BasicConnectShowActivity implements View.OnClickListener, BasicMvpInterface, NetChangeListener {
    private static final int TYPE_ETH_SIGN_SUCCESS = 0;
    private static final int TYPE_ETH_SIGN_FAILED = 1;
    private static final int TYPE_ERC20_SIGN_SUCCESS = 2;
    private static final int TYPE_ERC20_SIGN_FAILED = 3;
    private static final int TYPE_ACL_SIGN_SUCCESS = 4;
    private static final int TYPE_ACL_SIGN_FAILED = 5;
    private static final int TYPE_UPDATE_BTC_FEE = 6;
    private static final int TYPE_BTC_SIGN_SUCCESS = 7;
    private static final int TYPE_BTC_SIGN_FAILED = 8;
    private SeekBar seekbar;
    private int pro;
    private ImageView img_icon;
    private EditText edt_payment_address;
    private RelativeLayout rl_address_book, rl_scan;
    private EditText edt_transfer_amount;
    //    private EditText edt_transfer_tips;
    private TextView tv_mining_fee_value;
    private TextView tv_next;
    private SwipeRefreshLayout swipeRefreshLayout;

    /***2.0 转账界面修改变量***/
    private TextView tv_chain_type;
    private TextView tv_wallet_address;
    private TextView tv_chain_name;
    private TextView tv_amount;
    private TextView tv_assets_name;
    private TextView tv_amount_value;

    private ImageView img_enter_coin;
    private TextView tv_enter_coin;
    private TextView tv_fee_value;
    /**
     * 钱包信息
     */
    private WalletInfo walletInfo;
    private double amount;

    /**
     * 资产信息
     */
    private AssetsDetailsItemEntity assetsDetailsItemEntity;
    /**
     * 获取交易签名需要的参数
     */
    private BigDecimal gasPrice;
    private BigDecimal gasPriceOnline;
    private BigDecimal gasLimit = new BigDecimal(60000);
    private BigDecimal aclGasLimit = new BigDecimal(22000);
    private BigDecimal aclGasPriceParams;
    private BigDecimal aclGasPrice;
    private BigDecimal aclGasPriceOnline;
    private BigInteger nonce;
    private BigDecimal gasPriceParams;

    private TransferPresenter presenter;

    /**
     * 当前需要交易的货币类型
     */
    private String transferCoinName;
    /**
     * 用于记录交易及时反馈的信息
     */
    private TransferRecode transferRecode;

    private TransferDetailsDialog transferDetailsDialog;
    private PasswordDialog passwordDialog;

    private TransferBroadcastReceiver mTransferBroadcastReceiver;
    private String toAddress;

    private TipsDialog connectDialog;

    private int speed = 0;


    /**
     * BTC余额是否充足
     */
    private boolean btcIsEnough;
    /**
     * BTC gasPrice
     */
    private int btcGasPrice;

    /***各个货币余额***/
    private double aclBlance;
    private double btcBlance;
    private double ethBlance;
    private double erc20Blance;

    private RelativeLayout root_view;

    /***ACL 单笔交易标识***/
    private boolean aclIsTrading = false;
    private TipsDialog aclIsTradingDialog;

    /***矿工费货币估值***/
    private double feeValue = -1;
    /***货币估值***/
    private double coinValue = -1;

    @Override

    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_transfer);

    }

    @Override
    protected void setData() {
        if (presenter == null) {
            presenter = new TransferPresenter(this, this);
        }
        if (assetsDetailsItemEntity.getAssetsName().equals("ETH")) {
            Map<String, String> params = new HashMap<>();
            LogUtils.log("切换钱包之后的钱包地址是 = " + walletInfo.getAddress());
            params.put("address", walletInfo.getAddress());
            presenter.checkETHCoinValue(this);
            presenter.getData(params);
            presenter.getETHGasPrice();
            presenter.getEHTNonce(walletInfo.getAddress());
        } else if (assetsDetailsItemEntity.getAssetsName().equals("BTC")) {
            presenter.checkBTCCoinValue(this);
            presenter.getBTCGasPrice();
            presenter.getBTCUtxo(walletInfo.getAddress());
            presenter.checkBTCBalance(walletInfo.getAddress());
        } else if (assetsDetailsItemEntity.getAssetsName().equals("ACL")) {
            presenter.checkACLCoinValue(this);
            presenter.checkACLBalance(walletInfo.getAddress());
            presenter.getACLGasPrice();
            presenter.getACLNonce(walletInfo.getAddress());
            aclSingleTransfer(presenter);
        } else {
            LogUtils.log("请求ERC20余额" + assetsDetailsItemEntity.getContractAddress());
            presenter.checkETHCoinValue(this);
            presenter.checkErc20CoinValue(this, assetsDetailsItemEntity.getContractAddress());
            presenter.checkERC20Blance(walletInfo.getAddress(), assetsDetailsItemEntity.getContractAddress());
            presenter.getEthBlance(walletInfo.getAddress());
            presenter.getETHGasPrice();
            presenter.getEHTNonce(walletInfo.getAddress());
        }
    }

    /***ACL 执行单笔交易，交易成功一笔执行第二笔***/
    private void aclSingleTransfer(TransferPresenter presenter) {
        /***交易等待记录***/
        List<TransferWaitEntity> transferWaitEntities = TransferWaitSharedPreferences.getInstance(this).getTransferWaitList(walletInfo.getAddress(), "ACL");
        if (transferWaitEntities.size() > 0) {
            aclIsTrading = true;
            String aclHash = transferWaitEntities.get(0).getTransferRecode().getHash();
            LogUtils.log(className + " -- aclHash == " + aclHash);
            presenter.getACLTransferInfo(aclHash);
        } else {
            aclIsTrading = false;
        }
    }

    @Override
    protected void handleMsg(Message msg) {
        switch (msg.what) {
            case TYPE_ETH_SIGN_SUCCESS:
                String result = (String) msg.obj;
                Map<String, String> params = new HashMap<>();
                params.put("hexValue", result);
                presenter.ethTransfer(params);
//                if (swipeRefreshLayout.isRefreshing()) {
//                    swipeRefreshLayout.setRefreshing(false);
//                }
                break;
            case TYPE_ETH_SIGN_FAILED:
                ResultCode resultCode = (ResultCode) msg.obj;
                LogUtils.log(className + " -- ETH签名失败=" + resultCode);
                if (swipeRefreshLayout.isRefreshing()) {
                    swipeRefreshLayout.setRefreshing(false);
                }
                showToast(R.string.transfer_failed);
                break;
            case TYPE_ERC20_SIGN_SUCCESS:
                String erc20Result = (String) msg.obj;
                LogUtils.log(className + " -- ERC20 签名成功=" + erc20Result);
                Map<String, String> erc20Params = new HashMap<>();
                erc20Params.put("hexValue", erc20Result);
                presenter.ethTransfer(erc20Params);
                break;
            case TYPE_ERC20_SIGN_FAILED:
                ResultCode erc20ResultCode = (ResultCode) msg.obj;
                LogUtils.log("ERC20签名失败=" + erc20ResultCode);
                if (swipeRefreshLayout.isRefreshing()) {
                    swipeRefreshLayout.setRefreshing(false);
                }
                showToast(R.string.transfer_failed);
                break;
            case TYPE_ACL_SIGN_SUCCESS:
                String aclSgin = (String) msg.obj;
                LogUtils.log(className + " -- ACL 签名成功=" + aclSgin);
                Map<String, String> aclParams = new HashMap<>();
                aclParams.put("hexValue", aclSgin);
                presenter.aclTransfer(aclParams);
                break;
            case TYPE_ACL_SIGN_FAILED:
                ResultCode aclErrCode = (ResultCode) msg.obj;
                LogUtils.log(className + " -- ACL 签名失败=" + aclErrCode);
                if (swipeRefreshLayout.isRefreshing()) {
                    swipeRefreshLayout.setRefreshing(false);
                }
                showToast(R.string.transfer_failed);
                break;
            case TYPE_BTC_SIGN_SUCCESS:
                String btcSign = (String) msg.obj;
                Map<String, String> btcParams = new HashMap<>();
                btcParams.put("hex", btcSign);
                presenter.btcTransfer(btcParams);
                break;
            case TYPE_BTC_SIGN_FAILED:
                if (swipeRefreshLayout.isRefreshing()) {
                    swipeRefreshLayout.setRefreshing(false);
                }
                showToast(R.string.password_wrong);
                break;
            case TYPE_UPDATE_BTC_FEE:
                if (getBtcFee() == -1) {
                    String tips = String.format(getResources().getString(R.string.money_not_enough), getResources().getString(R.string.btc));
                    tv_mining_fee_value.setText(tips);
                    return;
                }
                tv_mining_fee_value.setText(new BigDecimal(getBtcFee()).divide(new BigDecimal(Math.pow(10, 8))) + getResources().getString(R.string.btc));
                if (feeValue != -1) {
                    double btcFeeValue = new BigDecimal(getBtcFee()).divide(new BigDecimal(Math.pow(10, 8))).doubleValue() * feeValue;
                    CommonUtils.setCurrency(tv_fee_value, MathUtils.doubleKeep2(btcFeeValue), true);
                }
                break;


        }
    }

    @Override
    protected void initReceiver() {
        if (mTransferBroadcastReceiver == null) {
            mTransferBroadcastReceiver = new TransferBroadcastReceiver();
        }
        IntentFilter filter = new IntentFilter();
        filter.addAction(IntentFilterConstant.CHANGE_ASSETS_TYPE);
        registerReceiver(mTransferBroadcastReceiver, filter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mTransferBroadcastReceiver);
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void initView() {
        LogUtils.log("initView");
        walletInfo = (WalletInfo) getIntent().getSerializableExtra("_walletInfo");
        assetsDetailsItemEntity = (AssetsDetailsItemEntity) getIntent().getSerializableExtra("_assetsDetailsItemEntity");
        transferCoinName = assetsDetailsItemEntity.getAssetsName();
        toAddress = getIntent().getStringExtra("_toAddress");

        LogUtils.log("合约地址是 =" + assetsDetailsItemEntity.getContractAddress());
        LogUtils.log("转账界面获取的交易精度是 =" + assetsDetailsItemEntity.getDecimals());

        mActionBar.setVisibility(View.VISIBLE);
        mActionBar.setActionTitle(getResources().getString(R.string.transfer));
        ViewCommonUtils.buildBackImageView(this, mActionBar);

        seekbar = findViewById(R.id.seekbar);
        img_icon = findViewById(R.id.img_icon);
        edt_payment_address = findViewById(R.id.edt_payment_address);
        rl_address_book = findViewById(R.id.rl_address_book);
        rl_scan = findViewById(R.id.rl_scan);
        edt_transfer_amount = findViewById(R.id.edt_transfer_amount);
//        edt_transfer_tips = findViewById(R.id.edt_transfer_tips);
        tv_mining_fee_value = findViewById(R.id.tv_mining_fee_value);
        tv_next = findViewById(R.id.tv_next);
        swipeRefreshLayout = findViewById(R.id.swipeRefreshLayout);
        //2.0转账
        tv_chain_type = findViewById(R.id.tv_chain_type);
        tv_wallet_address = findViewById(R.id.tv_wallet_address);
        tv_chain_name = findViewById(R.id.tv_chain_name);
        tv_amount = findViewById(R.id.tv_amount);
        img_enter_coin = findViewById(R.id.img_enter_coin);
        tv_enter_coin = findViewById(R.id.tv_enter_coin);
        root_view = findViewById(R.id.root_view);
        tv_fee_value = findViewById(R.id.tv_fee_value);
        tv_assets_name = findViewById(R.id.tv_assets_name);
        tv_amount_value = findViewById(R.id.tv_amount_value);

        tv_wallet_address.setText(walletInfo.getAddress());
        tv_chain_type.setText(walletInfo.getWalletName());
        tv_assets_name.setText(assetsDetailsItemEntity.getAssetsName());
        CommonUtils.setCurrency(tv_amount_value, "0.00", false);
        swipeRefreshLayout.setEnabled(false);
        swipeRefreshLayout.setRefreshing(true);


        if (assetsDetailsItemEntity.getAssetsName().equals("ETH")) {
            img_icon.setImageResource(R.drawable.eth_icon_selected);
            img_enter_coin.setVisibility(View.VISIBLE);
            tv_enter_coin.setVisibility(View.VISIBLE);
        } else if (assetsDetailsItemEntity.getAssetsName().equals("BTC")) {
            img_icon.setImageResource(R.drawable.btc);
            img_enter_coin.setVisibility(View.GONE);
            tv_enter_coin.setVisibility(View.GONE);
        } else if (assetsDetailsItemEntity.getAssetsName().equals("ACL")) {
            img_icon.setImageResource(R.drawable.acl_icon_select);
            img_enter_coin.setVisibility(View.GONE);
            tv_enter_coin.setVisibility(View.GONE);
        } else {
            ImageLoaderHelper.getInstance().loadImage(this, img_icon, assetsDetailsItemEntity.getIconUrl());
            img_enter_coin.setVisibility(View.VISIBLE);
            tv_enter_coin.setVisibility(View.VISIBLE);
        }

        if (toAddress != null) {
            edt_payment_address.setText(toAddress);
        }

        //BTC金额动态监听
        edt_transfer_amount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String strValue = s.toString();
                if (walletInfo.getChain() == Chain.BTC) {
                    mHandler.removeMessages(TYPE_UPDATE_BTC_FEE);
                    if (!s.toString().equals("")) {
                        mHandler.sendEmptyMessageDelayed(TYPE_UPDATE_BTC_FEE, 300);
                    }
                }
                if (coinValue != -1) {
                    try {
                        CommonUtils.setCurrency(tv_amount_value, MathUtils.doubleKeep2(Double.parseDouble(strValue) * coinValue), false);
                    } catch (NumberFormatException e) {
                        CommonUtils.setCurrency(tv_amount_value, "0.00", false);
                    }
                }
                String toAddress = CommonUtils.replaceEnterAndSpace(ViewCommonUtils.getEdtString(edt_payment_address));
                String transferValue = s.toString();
                if (!toAddress.equals("") && !transferValue.equals("")) {
                    if (walletInfo.getChain() == Chain.BTC) {
                        if (AddressUtils.isBTCValidAddress(toAddress)) {
                            if (WalletApplication.getApp().isNetWork()) {
                                tv_next.setBackgroundResource(R.drawable.btn_collect_copy);
                            } else {
                                tv_next.setBackgroundResource(R.drawable.radius_btn_noenabled);
                            }
                        } else {
                            tv_next.setBackgroundResource(R.drawable.radius_btn_noenabled);
                        }
                    } else {
                        if (AddressUtils.isETHValidAddress(toAddress)) {
                            if (WalletApplication.getApp().isNetWork()) {
                                tv_next.setBackgroundResource(R.drawable.btn_collect_copy);
                            } else {
                                tv_next.setBackgroundResource(R.drawable.radius_btn_noenabled);
                            }
                        } else {
                            tv_next.setBackgroundResource(R.drawable.radius_btn_noenabled);
                        }
                    }
                } else {
                    tv_next.setBackgroundResource(R.drawable.radius_btn_noenabled);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        edt_payment_address.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String toAddress = s.toString();
                String transferValue = ViewCommonUtils.getEdtString(edt_transfer_amount);
                if (!toAddress.equals("") && !transferValue.equals("")) {
                    if (walletInfo.getChain() == Chain.BTC) {
                        if (AddressUtils.isBTCValidAddress(toAddress)) {
                            if (WalletApplication.getApp().isNetWork()) {
                                tv_next.setBackgroundResource(R.drawable.btn_collect_copy);
                            } else {
                                tv_next.setBackgroundResource(R.drawable.radius_btn_noenabled);
                            }
                        } else {
                            tv_next.setBackgroundResource(R.drawable.radius_btn_noenabled);
                        }
                    } else {
                        if (AddressUtils.isETHValidAddress(toAddress)) {
                            if (WalletApplication.getApp().isNetWork()) {
                                tv_next.setBackgroundResource(R.drawable.btn_collect_copy);
                            } else {
                                tv_next.setBackgroundResource(R.drawable.radius_btn_noenabled);
                            }
                        } else {
                            tv_next.setBackgroundResource(R.drawable.radius_btn_noenabled);
                        }
                    }
                } else {
                    tv_next.setBackgroundResource(R.drawable.radius_btn_noenabled);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        /**
         * 解决SEEKBAR闪烁的问题，在跟布局拦截事件
         */
        root_view.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });

        /**
         * 进度条
         */

        seekbar.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (v.getId() == R.id.seekbar) {
                    if (event.getAction() == MotionEvent.ACTION_UP) {
                        if (pro < 100) {
                            pro += 50;
                        } else {
                            pro = 0;
                        }
                        seekbar.setProgress(pro);
                        switch (walletInfo.getChain()) {
                            case BTC:
                                if (seekbar.getProgress() == 0) {
                                    btcGasPrice = recommandGasPrice.getHourFee();
                                } else if (seekbar.getProgress() == 50) {
                                    btcGasPrice = recommandGasPrice.getHalfHourFee();
                                } else if (seekbar.getProgress() == 100) {
                                    btcGasPrice = recommandGasPrice.getFastestFee();
                                }

                                if (!ViewCommonUtils.getEdtString(edt_transfer_amount).equals("") && getBtcFee() != -1) {
                                    tv_mining_fee_value.setText(new BigDecimal(getBtcFee()).divide(new BigDecimal(Math.pow(10, 8))) + getResources().getString(R.string.btc));
                                    if (feeValue != -1) {
                                        double btcFeeValue = new BigDecimal(getBtcFee()).divide(new BigDecimal(Math.pow(10, 8))).doubleValue() * feeValue;
                                        CommonUtils.setCurrency(tv_fee_value, MathUtils.doubleKeep2(btcFeeValue), true);
                                    }
                                } else {
                                    String tips = String.format(getResources().getString(R.string.money_not_enough), getResources().getString(R.string.btc));
                                    tv_mining_fee_value.setText(tips);
                                }
                                break;
                            case ACL:
                                if (seekbar.getProgress() == 0) {
                                    //慢
                                    aclGasPrice = aclGasPriceOnline.multiply(new BigDecimal(1.0));
                                    aclGasPriceParams = aclGasPrice.divide(new BigDecimal(Math.pow(10, 9)));
                                    tv_mining_fee_value.setText(MathUtils.getACLMinersFee(aclGasPrice, 0) + " ACL");
                                    speed = 0;
                                    if (feeValue != -1) {
                                        double d = Double.parseDouble(MathUtils.getACLMinersFee(aclGasPrice, speed)) * feeValue;

                                        CommonUtils.setCurrency(tv_fee_value, MathUtils.doubleKeep2(d), true);
                                    }
                                } else if (seekbar.getProgress() == 50) {
                                    //中
                                    aclGasPrice = aclGasPriceOnline.multiply(new BigDecimal(1.1));
                                    aclGasPriceParams = aclGasPrice.divide(new BigDecimal(Math.pow(10, 9)));
                                    tv_mining_fee_value.setText(MathUtils.getACLMinersFee(aclGasPrice, 1) + " ACL");
                                    speed = 1;
                                    if (feeValue != -1) {
                                        double d = Double.parseDouble(MathUtils.getACLMinersFee(aclGasPrice, speed)) * feeValue;
                                        CommonUtils.setCurrency(tv_fee_value, MathUtils.doubleKeep2(d), true);
                                    }
                                } else if (seekbar.getProgress() == 100) {
                                    //快
                                    aclGasPrice = aclGasPriceOnline.multiply(new BigDecimal(1.2));
                                    aclGasPriceParams = aclGasPrice.divide(new BigDecimal(Math.pow(10, 9)));
                                    tv_mining_fee_value.setText(MathUtils.getACLMinersFee(aclGasPrice, 2) + " ACL");
                                    speed = 2;
                                    if (feeValue != -1) {
                                        double d = Double.parseDouble(MathUtils.getACLMinersFee(aclGasPrice, speed)) * feeValue;
                                        CommonUtils.setCurrency(tv_fee_value, MathUtils.doubleKeep2(d), true);
                                    }
                                }
                                break;
                            case ETH:
                                if (seekbar.getProgress() == 0) {
                                    //慢
                                    gasPrice = gasPriceOnline.multiply(new BigDecimal(Constant.EthGasPriceSpeed.TYPE_SPEED_SLOW));
                                    gasPriceParams = gasPrice.divide(new BigDecimal(Math.pow(10, 9)));
                                    tv_mining_fee_value.setText(MathUtils.getMinersFee(gasPrice, 0) + " ETH");
                                    speed = 0;
                                    if (feeValue != -1) {
                                        double d = Double.parseDouble(MathUtils.getMinersFee(gasPrice, speed)) * feeValue;
                                        CommonUtils.setCurrency(tv_fee_value, MathUtils.doubleKeep2(d), true);
                                    }
                                } else if (seekbar.getProgress() == 50) {
                                    //中
                                    gasPrice = gasPriceOnline.multiply(new BigDecimal(Constant.EthGasPriceSpeed.TYPE_SPEED_NORMAL));
                                    gasPriceParams = gasPrice.divide(new BigDecimal(Math.pow(10, 9)));
                                    tv_mining_fee_value.setText(MathUtils.getMinersFee(gasPrice, 1) + " ETH");
                                    speed = 1;
                                    if (feeValue != -1) {
                                        double d = Double.parseDouble(MathUtils.getMinersFee(gasPrice, speed)) * feeValue;
                                        CommonUtils.setCurrency(tv_fee_value, MathUtils.doubleKeep2(d), true);
                                    }
                                } else if (seekbar.getProgress() == 100) {
                                    //快
                                    gasPrice = gasPriceOnline.multiply(new BigDecimal(Constant.EthGasPriceSpeed.TYPE_SPEED_FAST));
                                    gasPriceParams = gasPrice.divide(new BigDecimal(Math.pow(10, 9)));
                                    tv_mining_fee_value.setText(MathUtils.getMinersFee(gasPrice, 2) + " ETH");
                                    speed = 2;
                                    if (feeValue != -1) {
                                        double d = Double.parseDouble(MathUtils.getMinersFee(gasPrice, speed)) * feeValue;
                                        CommonUtils.setCurrency(tv_fee_value, MathUtils.doubleKeep2(d), true);
                                    }
                                }
                                break;
                        }
                    }
                }
                return true;
            }
        });

        rl_address_book.setOnClickListener(this);
        rl_scan.setOnClickListener(this);
        tv_next.setOnClickListener(this);
        /**
         * ACL有交易正在进行弹窗
         */
        DialogEntity aclIsTradingDialogEntity = new DialogEntity(getResources().getString(R.string.tips),
                getResources().getString(R.string.acl_single_transfer),
                getResources().getString(R.string.ok),
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (aclIsTradingDialog != null) {
                            if (aclIsTradingDialog.isShowing()) {
                                aclIsTradingDialog.dismiss();
                            }
                        }
                    }
                });
        aclIsTradingDialog = new TipsDialog(this, aclIsTradingDialogEntity, false);
        /**
         * 硬件提示
         */
        DialogEntity dialogEntity = new DialogEntity(getResources().getString(R.string.hardware_not_connect),
                getResources().getString(R.string.hardware_not_connect_desc),
                getResources().getString(R.string.ok),
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (connectDialog.isShowing()) {
                            connectDialog.dismiss();
                        }
                    }
                });
        connectDialog = new TipsDialog(this, dialogEntity);

        transferRecode = new TransferRecode();
        /**
         * 密码输入框
         */
        passwordDialog = new PasswordDialog(this, R.style.Wallet_Manager_Dialog, 1, false, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String pwd = passwordDialog.getEdtText();
                final String transferValue = ViewCommonUtils.getEdtString(edt_transfer_amount);
                final String toAddress = CommonUtils.replaceEnterAndSpace(ViewCommonUtils.getEdtString(edt_payment_address));
//                final String remark = CommonUtils.replaceEnter(CommonUtils.str2HexStr(ViewCommonUtils.getEdtString(edt_transfer_tips)));
                if (!pwd.equals("")) {
                    try {
                        startTransfer(pwd, transferValue, toAddress, "", walletInfo);
                    } catch (Exception e) {
                        LogUtils.log(className + " -- 交易出错" + e.toString());
                    }
                } else {
                    showToast(R.string.password_isempty);
                }
            }
        });
    }

    /**
     * 开始交易
     *
     * @param pwd
     * @param transferValue
     * @param toAddress
     * @param remark
     */
    private void startTransfer(String pwd, String transferValue, String toAddress, String remark, WalletInfo walletInfo) {
        if (passwordDialog.isShowing()) {
            passwordDialog.dismiss();
        }
        swipeRefreshLayout.setRefreshing(true);
        if (assetsDetailsItemEntity.getAssetsName().equals("ETH")) {
            LogUtils.log(className + " -- ETH交易签名  参数 pwd =" + pwd + " -- transferValue == " + transferValue + " -- remark==" + remark + " -- toAddress==" + toAddress);
            ethSign(pwd, transferValue, remark, toAddress);
        } else if (assetsDetailsItemEntity.getAssetsName().equals("BTC")) {
            LogUtils.log(className + " -- BTC交易签名");
            btcSign(pwd, transferValue, toAddress);
        } else if (assetsDetailsItemEntity.getAssetsName().equals("ACL")) {
            LogUtils.log("ACL交易签名");
            aclSign(pwd, transferValue, remark, toAddress);
        } else {
            LogUtils.log("ERC20交易签名");
            erc20Sign(pwd, transferValue, remark, toAddress);
        }
    }

    /**
     * BTC签名
     *
     * @param pwd
     * @param transferValue
     * @param toAddress
     */
    private void btcSign(String pwd, String transferValue, String toAddress) {
        Map<String, Object> params = new HashMap<>();
        params.put("transferType", TransferType.BITCOIN_SEGWIT);
        params.put("to", toAddress);
        params.put("value", new BigDecimal(transferValue).multiply(new BigDecimal(Math.pow(10, 8))).longValue());
        params.put("utxoList", utxoList);
        params.put("fee", getBtcFee());

        LogUtils.log("BTC交易参数 ==" + params.toString());


        WalletManager.getInstance().signTransaction(this, walletInfo, pwd, params, new IRequestListener<String>() {
            @Override
            public void onResult(ResultCode resultCode, String result) {
                switch (resultCode) {
                    case SUCCESS:
                        LogUtils.log("btc交易签名成功 = " + result);
                        Message message = Message.obtain();
                        message.what = TYPE_BTC_SIGN_SUCCESS;
                        message.obj = result;
                        mHandler.sendMessage(message);
                        break;
                    default:
                        LogUtils.log("btc交易签名失败 =" + resultCode);
                        mHandler.sendEmptyMessage(TYPE_BTC_SIGN_FAILED);
                        break;
                }
            }
        });
    }

    /**
     * ACL签名
     *
     * @param pwd
     * @param transferValue
     * @param remark
     * @param toAddress
     */
    private void aclSign(String pwd, String transferValue, String remark, String toAddress) {
        Map<String, Object> params = new HashMap<>();
        params.put("transferType", TransferType.APOCALYPSE_ACL);
        params.put("to", toAddress);
        params.put("nonce", nonce);
        params.put("gasPrice", aclGasPriceParams);
        params.put("gasLimit", aclGasLimit);
        params.put("value", new BigDecimal(transferValue).multiply(new BigDecimal(Math.pow(10, 18))).toBigInteger());
        params.put("data", remark);
        LogUtils.log("ACL交易参数 ==" + params.toString());

        WalletManager.getInstance().signTransaction(
                TransferActivity.this,
                walletInfo,
                pwd,
                params,
                new IRequestListener<String>() {
                    @Override
                    public void onResult(ResultCode resultCode, String result) {
                        switch (resultCode) {
                            case SUCCESS:
                                Message message = Message.obtain();
                                message.what = TYPE_ACL_SIGN_SUCCESS;
                                message.obj = result;
                                mHandler.sendMessage(message);
                                break;
                            case FAIL:
                                Message failMessage = Message.obtain();
                                failMessage.what = TYPE_ACL_SIGN_FAILED;
                                failMessage.obj = resultCode;
                                mHandler.sendMessage(failMessage);
                                break;
                            default:
                                showToast(R.string.password_wrong);
                                if (swipeRefreshLayout.isRefreshing()) {
                                    swipeRefreshLayout.setRefreshing(false);
                                }
                                break;

                        }
                    }
                });
    }

    /**
     * ETH交易签名
     *
     * @param pwd
     */
    private void ethSign(String pwd, String transferValue, String remark, String toAddress) {
        Map<String, Object> params = new HashMap<>();
        params.put("transferType", TransferType.ETHEREUM_ETH);
        params.put("to", toAddress);
        params.put("nonce", nonce);
        params.put("gasPrice", gasPriceParams);
        params.put("gasLimit", gasLimit);
        params.put("value", new BigDecimal(transferValue).multiply(new BigDecimal(Math.pow(10, 18))).toBigInteger());
        params.put("data", remark);
        LogUtils.log("ETH交易参数 ==" + params.toString());
        WalletManager.getInstance().signTransaction(
                TransferActivity.this,
                walletInfo,
                pwd,
                params,
                new IRequestListener<String>() {
                    @Override
                    public void onResult(ResultCode resultCode, String result) {
                        switch (resultCode) {
                            case SUCCESS:
                                LogUtils.log(className + " -- ETH签名成功=" + result);
                                Message message = Message.obtain();
                                message.what = TYPE_ETH_SIGN_SUCCESS;
                                message.obj = result;
                                mHandler.sendMessage(message);
                                break;
                            case FAIL:
                                Message failMessage = Message.obtain();
                                failMessage.what = TYPE_ETH_SIGN_FAILED;
                                failMessage.obj = resultCode;
                                mHandler.sendMessage(failMessage);
                                break;
                            default:
                                showToast(R.string.password_wrong);
                                if (swipeRefreshLayout.isRefreshing()) {
                                    swipeRefreshLayout.setRefreshing(false);
                                }
                                break;

                        }
                    }
                });
    }

    /**
     * ERC20交易签名
     */
    private void erc20Sign(String pwd, String transferValue, String remark, String toAddress) {

        Map<String, Object> params = new HashMap<>();
        params.put("transferType", TransferType.ETHEREUM_ERC20);
        params.put("erc20ContractAddress", assetsDetailsItemEntity.getContractAddress());
        params.put("to", toAddress);
        params.put("nonce", nonce);
        params.put("gasPrice", gasPriceParams);
        params.put("gasLimit", gasLimit);
        params.put("value", new BigDecimal(transferValue).multiply(new BigDecimal(Math.pow(10, assetsDetailsItemEntity.getDecimals()))).toBigInteger());
        LogUtils.log("ERC20交易签名精度 = " + assetsDetailsItemEntity.getDecimals());
        LogUtils.log("ERC20交易签名参数 = " + params.toString());

        WalletManager.getInstance().signTransaction(
                TransferActivity.this,
                walletInfo,
                pwd,
                params,
                new IRequestListener<String>() {
                    @Override
                    public void onResult(ResultCode resultCode, String result) {

                        switch (resultCode) {
                            case SUCCESS:
                                LogUtils.log("ERC20签名成功=" + result);
                                Message message = Message.obtain();
                                message.what = TYPE_ERC20_SIGN_SUCCESS;
                                message.obj = result;
                                mHandler.sendMessage(message);
                                break;
                            case FAIL:
                                Message failMessage = Message.obtain();
                                failMessage.what = TYPE_ERC20_SIGN_FAILED;
                                failMessage.obj = resultCode;
                                mHandler.sendMessage(failMessage);
                                break;
                            default:
                                showToast(R.string.password_wrong);
                                if (swipeRefreshLayout.isRefreshing()) {
                                    swipeRefreshLayout.setRefreshing(false);
                                }
                                break;

                        }
                    }
                });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case 1:
                if (resultCode == this.RESULT_OK) {
                    Bundle bundle = data.getExtras();
                    String string = bundle.getString("result");
                    if (string.contains("&")) {
                        String address = string.split("&")[0];
                        String value = string.split("&")[1];
                        edt_payment_address.setText(address);
                        edt_transfer_amount.setText(value);
                    } else {
                        edt_payment_address.setText(string);
                    }

                }
                break;
            case Constant.StartActivityResultCode.START_ADDRESS_BOOK:
                if (resultCode == 0x20) {
                    Bundle bundle = data.getExtras();
                    edt_payment_address.setText(bundle.getString("addressResult"));
                }
                break;
            case Constant.StartActivityResultCode.START_WALLET_MANAGER:
                if (resultCode == 0x88) {
                    Bundle bundle = data.getExtras();
                    walletInfo = (WalletInfo) bundle.getSerializable("_walletInfo");
                    tv_chain_type.setText(walletInfo.getWalletName());
                    tv_wallet_address.setText(walletInfo.getAddress());
                    setData();
                }
                break;
            case Constant.StartActivityResultCode.START_SELECT_ASSETS:
                if (resultCode == 0x26) {
                    Bundle bundle = data.getExtras();
                    assetsDetailsItemEntity = (AssetsDetailsItemEntity) bundle.getSerializable("_assetsDetailsBack");
                    LogUtils.log("选择货币之后的对象精度是 =" + assetsDetailsItemEntity.getDecimals());
                    transferCoinName = assetsDetailsItemEntity.getAssetsName();
                    edt_transfer_amount.setText("");
                    tv_assets_name.setText(assetsDetailsItemEntity.getAssetsName());
                    if (!swipeRefreshLayout.isRefreshing()) {
                        swipeRefreshLayout.setRefreshing(true);
                    }
                    setData();
                }
                break;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_change_wallet:
                break;
            case R.id.rl_change_coin:
                break;
            case R.id.rl_address_book:
                Intent addressBookIntent = new Intent(this, AddressBookActivity.class);
                addressBookIntent.putExtra("_address_book_mark", Constant.AddressBookMark.TYPE_TRANSFER);
                startActivityForResult(addressBookIntent, Constant.StartActivityResultCode.START_ADDRESS_BOOK);
                break;
            case R.id.rl_scan:
                CommonUtils.openScan(this);
                break;
            case R.id.tv_next:
                String toAddress = CommonUtils.replaceEnterAndSpace(ViewCommonUtils.getEdtString(edt_payment_address));
                String transferValue = ViewCommonUtils.getEdtString(edt_transfer_amount);
                if (!toAddress.equals("")) {
                    if (!transferValue.equals("")) {
                        if (assetsDetailsItemEntity.getAssetsName().equals("ETH")) {
                            if (AddressUtils.isETHValidAddress(toAddress)) {
                                feeCompareBlance(ethBlance, assetsDetailsItemEntity.getAssetsName(), transferValue, "", toAddress);
                            } else {
                                showToast(R.string.not_ETH_address);
                            }
                        } else if (assetsDetailsItemEntity.getAssetsName().equals("BTC")) {
                            if (AddressUtils.isBTCValidAddress(toAddress)) {
                                feeCompareBlance(btcBlance, assetsDetailsItemEntity.getAssetsName(), transferValue, "", toAddress);
                            } else {
                                showToast(R.string.not_BTC_address);
                            }
                        } else if (assetsDetailsItemEntity.getAssetsName().equals("ACL")) {
                            if (AddressUtils.isETHValidAddress(toAddress)) {
                                if (aclIsTrading) {
                                    showToast(R.string.acl_single_transfer);
                                } else {
                                    feeCompareBlance(aclBlance, assetsDetailsItemEntity.getAssetsName(), transferValue, "", toAddress);
                                }
                            } else {
                                showToast(R.string.not_ACL_address);
                            }
                        } else {
                            if (AddressUtils.isETHValidAddress(toAddress)) {
                                String tips = String.format(getResources().getString(R.string.money_not_enough), assetsDetailsItemEntity.getAssetsName());
                                String fee = tv_mining_fee_value.getText().toString().replace("ETH", "").trim();
                                try {
                                    double feeDouble = Double.parseDouble(fee);
                                    double value = Double.parseDouble(transferValue.trim());
                                    if (value > erc20Blance) {
                                        showToast(tips);
                                    } else {
                                        if (feeDouble > ethBlance) {
                                            showToast(R.string.fee_not_enough);
                                        } else {
                                            showTransferDetails("", transferValue, toAddress);
                                        }
                                    }
                                } catch (NumberFormatException e) {
                                    showToast(R.string.fee_not_enough);
                                }
                            } else {
                                showToast(R.string.not_ETH_address);
                            }
                        }
                    } else {
                        showToast(R.string.value_isempty);
                    }
                } else {
                    showToast(R.string.to_address_isempty);
                }
                break;
            case R.id.btn_ok:
                transferDetailsDialog.dismiss();
                passwordDialog.setTitle(getResources().getString(R.string.dialog_password_1));
                passwordDialog.show();
                break;
        }
    }

    /**
     * 矿工费跟余额比较大小
     *
     * @param blance
     */
    private void feeCompareBlance(double blance, String coinName, String transferValue, String transferTips, String toAddress) {
        String tips = String.format(getResources().getString(R.string.money_not_enough), assetsDetailsItemEntity.getAssetsName());
        String fee = tv_mining_fee_value.getText().toString().replace(coinName, "").trim();
        try {
            double feeDouble = Double.parseDouble(fee);
            double value = Double.parseDouble(transferValue.trim());
            LogUtils.log(className + " -- 矿工费是 =" + feeDouble);
            LogUtils.log(className + " -- 转账金额是 =" + value);
            LogUtils.log(className + " -- 总需求金额是 =" + (feeDouble + value));
            LogUtils.log(className + " -- 余额是 =" + blance);
            if ((feeDouble + value) < blance) {
                showTransferDetails(transferTips, transferValue, toAddress);
            } else {
                showToast(tips);
            }
        } catch (NumberFormatException e) {
            showToast(tips);
        }
    }

    /**
     * 点击下一步显示交易确认弹窗
     *
     * @param transferTips
     * @param transferValue
     * @param toAddress
     */
    private void showTransferDetails(String transferTips, String transferValue, String toAddress) {
        LogUtils.log(className + " -- showTransferDetails toaddress == " + toAddress);
        transferRecode.setTo(toAddress);
        transferRecode.setFrom(walletInfo.getAddress());

        transferRecode.setInput(transferTips);
        if (getBtcFee() != -1) {
            transferRecode.setBtcFee(getBtcFee());
        }
        switch (walletInfo.getChain()) {
            case ETH:
                LogUtils.log(className + " -- 该货币的精度是 =" + assetsDetailsItemEntity.getDecimals());
                LogUtils.log(className + " -- 该货币的值是 =" + transferValue);
                LogUtils.log(className + " -- 该货币的值是 =" + new BigDecimal(transferValue).multiply(new BigDecimal(Math.pow(10, assetsDetailsItemEntity.getDecimals()))).toString());
                transferRecode.setTokenDecimal(assetsDetailsItemEntity.getDecimals() + "");
                transferRecode.setValue(new BigDecimal(transferValue).multiply(new BigDecimal(Math.pow(10, assetsDetailsItemEntity.getDecimals()))).toString());
                transferRecode.setCumulativeGasUsed(MathUtils.getMinersFeeBigDecimal(gasPrice, speed) + "");
                transferRecode.setGasPrice(gasPrice + "");
                transferRecode.setGasUsed(gasLimit + "");
                break;
            case BTC:
                transferRecode.setValue(new BigDecimal(transferValue).toString());
                break;
            case ACL:
                transferRecode.setValue(new BigDecimal(transferValue).multiply(new BigDecimal(Math.pow(10, 18))).toString());
                transferRecode.setCumulativeGasUsed(MathUtils.getACLMinersFeeBigDecimal(aclGasPrice, speed) + "");
                transferRecode.setGasPrice(aclGasPrice + "");
                transferRecode.setGasUsed(aclGasLimit + "");
                break;
        }


        if (seekbar.getProgress() == 0) {
            transferRecode.setSpeed(0);
        } else if (seekbar.getProgress() == 50) {
            transferRecode.setSpeed(1);
        } else if (seekbar.getProgress() == 100) {
            transferRecode.setSpeed(2);
        }
        transferDetailsDialog = new TransferDetailsDialog(this, R.style.Wallet_Manager_Dialog, transferCoinName, transferRecode, walletInfo, coinValue, feeValue, this);
        if (!toAddress.equals("")) {
            if (!transferValue.equals("")) {
                if (Double.parseDouble(transferValue) > amount) {
                    String tips = String.format(getResources().getString(R.string.money_not_enough), assetsDetailsItemEntity.getAssetsName());
                    LogUtils.log("余额不足");
                    showToast(tips);
                } else {
                    if (WalletApplication.getApp().isNetWork()) {
                        transferDetailsDialog.show();
                    } else {
                        tipsDialog.show();
                    }
                }
            } else {
                showToast(R.string.value_isempty);
            }
        } else {
            LogUtils.log("地址与交易金额有空");
            showToast(R.string.to_address_isempty);
        }
    }

    @Override
    public void getDataSuccess(Object object, int type) {
        switch (type) {
            case TransferPresenter.TYPE_CHECK_ETH_BALANCE:
                String doubleString = (String) object;
                amount = Double.parseDouble(doubleString);
                ethBlance = amount;
                tv_amount.setText(MathUtils.doubleKeep4(amount));
                tv_chain_name.setText(transferCoinName);
                img_icon.setImageResource(R.drawable.eth_icon_selected);
                break;
            case TransferPresenter.TYPE_GET_ETH_NONCE:
                LogUtils.log("请求到的nonce=" + object);
                String strNonce = (String) object;
                nonce = new BigInteger(strNonce);
                break;
            case TransferPresenter.TYPE_GET_ETH_GASPRICE:
                LogUtils.log("请求到的gasprice=" + object);
                String str = (String) object;
                gasPriceOnline = new BigDecimal(str);
                gasPrice = gasPriceOnline.multiply(new BigDecimal(Constant.EthGasPriceSpeed.TYPE_SPEED_SLOW));
                gasPriceParams = gasPrice.divide(new BigDecimal(Math.pow(10, 9)));
                //慢
                tv_mining_fee_value.setText(MathUtils.getMinersFee(gasPrice, 0) + " ETH");
                if (feeValue != -1) {
                    double d = Double.parseDouble(MathUtils.getMinersFee(gasPrice, speed)) * feeValue;
                    CommonUtils.setCurrency(tv_fee_value, MathUtils.doubleKeep2(d), true);
                }
                if (transferCoinName.equals("ETH")) {
                    if (swipeRefreshLayout.isRefreshing()) {
                        swipeRefreshLayout.setRefreshing(false);
                    }
                }
                break;
            case TransferPresenter.TYPE_ETH_TRANSFER:
                String hash = (String) object;
                transferRecode.setHash(hash);
                transferRecode.setTimestamp(System.currentTimeMillis() / 1000);
                transferRecode.setTransferType(3);
                if (swipeRefreshLayout.isRefreshing()) {
                    swipeRefreshLayout.setRefreshing(false);
                }

                Intent intent = new Intent(TransferActivity.this, TransferDetailsActivity.class);
                intent.putExtra("_transferRecode", transferRecode);
                intent.putExtra("_transferCoinName", transferCoinName);
                intent.putExtra("_transferType", 3);
                intent.putExtra("_walletInfo", walletInfo);
                startActivity(intent);
                TransferActivity.this.finish();
                break;
            case TransferPresenter.TYPE_CHECK_ERC20_BALANCE:
                LogUtils.log("获取ERC20余额成功");
                String erc20Result = (String) object;
                double erc20Balance = Double.parseDouble(erc20Result);
                this.erc20Blance = erc20Balance;
                tv_amount.setText(MathUtils.doubleKeep4(erc20Balance));
                amount = erc20Balance;
                tv_chain_name.setText(transferCoinName);
                ImageLoaderHelper.getInstance().loadImage(this, img_icon, assetsDetailsItemEntity.getIconUrl());
                if (swipeRefreshLayout.isRefreshing()) {
                    swipeRefreshLayout.setRefreshing(false);
                }
                break;
            case TransferPresenter.TYPE_ACL_TRANSFER:
                LogUtils.log(className + " -- ACL 交易回调成功");
                String aclHash = (String) object;
                transferRecode.setHash(aclHash);
                transferRecode.setTimestamp(System.currentTimeMillis() / 1000);
                transferRecode.setTransferType(3);
                if (swipeRefreshLayout.isRefreshing()) {
                    swipeRefreshLayout.setRefreshing(false);
                }

                Intent aclTransferIntent = new Intent(TransferActivity.this, TransferDetailsActivity.class);
                aclTransferIntent.putExtra("_transferRecode", transferRecode);
                aclTransferIntent.putExtra("_transferCoinName", transferCoinName);
                aclTransferIntent.putExtra("_transferType", 3);
                aclTransferIntent.putExtra("_walletInfo", walletInfo);
                startActivity(aclTransferIntent);
                TransferActivity.this.finish();
                break;
            case TransferPresenter.TYPE_GET_ACL_NONCE:
                LogUtils.log("请求到的ACL nonce=" + object);
                String aclStrNonce = (String) object;
                nonce = new BigInteger(aclStrNonce);
                break;
            case TransferPresenter.TYPE_GET_ACL_GASPRICE:
                LogUtils.log("请求到的ACL gasprice=" + object);
                String aclGasprice = (String) object;
                aclGasPriceOnline = new BigDecimal(aclGasprice);
                aclGasPrice = aclGasPriceOnline.multiply(new BigDecimal(1.0));
                aclGasPriceParams = aclGasPrice.divide(new BigDecimal(Math.pow(10, 9)));
                //慢
                tv_mining_fee_value.setText(MathUtils.getACLMinersFee(aclGasPrice, 0) + " ACL");
                if (feeValue != -1) {
                    double d = Double.parseDouble(MathUtils.getACLMinersFee(aclGasPrice, speed)) * feeValue;
                    CommonUtils.setCurrency(tv_fee_value, MathUtils.doubleKeep2(d), true);
                }
                if (transferCoinName.equals("ACL")) {
                    if (swipeRefreshLayout.isRefreshing()) {
                        swipeRefreshLayout.setRefreshing(false);
                    }
                }
                break;
            case TransferPresenter.TYPE_CHECK_ACL_BALANCE:
                String aclDoubleString = (String) object;
                amount = Double.parseDouble(aclDoubleString);
                aclBlance = amount;
                tv_amount.setText(MathUtils.doubleKeep4(amount));
                tv_chain_name.setText(transferCoinName);
                img_icon.setImageResource(R.drawable.acl_icon_select);
                break;

            case TransferPresenter.TYPE_CHECK_BTC_BALANCE:
                amount = (double) object;
                btcBlance = amount;
                tv_amount.setText(MathUtils.doubleKeep8(amount));
                tv_chain_name.setText(transferCoinName);
                img_icon.setImageResource(R.drawable.btc);
                break;
            case TransferPresenter.TYPE_GET_BTC_FEE:
                recommandGasPrice = (RecommandGasPrice) object;
                btcGasPrice = recommandGasPrice.getHourFee();

                if (transferCoinName.equals("BTC")) {
                    if (swipeRefreshLayout.isRefreshing()) {
                        swipeRefreshLayout.setRefreshing(false);
                    }
                }
                break;
            case TransferPresenter.TYPE_GET_BTC_UTXO:
                utxoList = (List<UTXO>) object;
                break;
            case TransferPresenter.TYPE_BTC_TRANSFER:
                LogUtils.log(className + " -- BTC 交易回调成功");
                String btcHash = (String) object;
                transferRecode.setHash(btcHash);
                transferRecode.setTimestamp(System.currentTimeMillis() / 1000);
                transferRecode.setTransferType(3);

                if (swipeRefreshLayout.isRefreshing()) {
                    swipeRefreshLayout.setRefreshing(false);
                }

                Intent btcTransferIntent = new Intent(TransferActivity.this, TransferDetailsActivity.class);
                btcTransferIntent.putExtra("_transferRecode", transferRecode);
                btcTransferIntent.putExtra("_transferCoinName", transferCoinName);
                btcTransferIntent.putExtra("_transferType", 3);
                btcTransferIntent.putExtra("_walletInfo", walletInfo);
                startActivity(btcTransferIntent);
                TransferActivity.this.finish();
                break;
            case TransferPresenter.TYPE_GET_ETH_BALANCE:
                String ethDoubleString = (String) object;
                ethBlance = Double.parseDouble(ethDoubleString);
                break;
            case TransferPresenter.TYPE_CHECK_ACL_TRANSFER_INFO:
                AclTransferInfoEntity entity = (AclTransferInfoEntity) object;
                if (!entity.isError()) {
                    aclIsTrading = false;
                    TransferWaitSharedPreferences.getInstance(this).removeRecord(walletInfo.getAddress(), "ACL", entity.getHash());
                } else {
                    aclIsTrading = true;
                    aclIsTradingDialog.show();
                }
                break;
            case TransferPresenter.TYPE_CHECK_ETH_VALUE:
                feeValue = Double.parseDouble((String) object);
                if (assetsDetailsItemEntity.getAssetsName().equals("ETH")) {
                    coinValue = Double.parseDouble((String) object);
                }
                if (gasPrice != null) {
                    double d = Double.parseDouble(MathUtils.getMinersFee(gasPrice, speed)) * feeValue;
                    CommonUtils.setCurrency(tv_fee_value, MathUtils.doubleKeep2(d), true);
                }
                break;
            case TransferPresenter.TYPE_CHECK_BTC_VALUE:
                feeValue = Double.parseDouble((String) object);
                coinValue = Double.parseDouble((String) object);
                if (getBtcFee() != -1) {
                    double btcFeeValue = new BigDecimal(getBtcFee()).divide(new BigDecimal(Math.pow(10, 8))).doubleValue() * feeValue;
                    CommonUtils.setCurrency(tv_fee_value, MathUtils.doubleKeep2(btcFeeValue), true);
                }
                break;
            case TransferPresenter.TYPE_CHECK_ACL_VALUE:
                feeValue = Double.parseDouble((String) object);
                coinValue = Double.parseDouble((String) object);
                if (aclGasPrice != null) {
                    double d = Double.parseDouble(MathUtils.getACLMinersFee(aclGasPrice, speed)) * feeValue;
                    CommonUtils.setCurrency(tv_fee_value, MathUtils.doubleKeep2(d), true);
                }
                break;
            case TransferPresenter.TYPE_CHECK_ERC20_VALUE:
                coinValue = Double.parseDouble((String) object);
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + type);
        }
    }

    @Override
    public void getDataFail(Object error, int type) {
        switch (type) {
            case TransferPresenter.TYPE_ACL_TRANSFER:
            case TransferPresenter.TYPE_BTC_TRANSFER:
            case TransferPresenter.TYPE_ETH_TRANSFER:
                showToast(R.string.transfer_failed);
                swipeRefreshLayout.setRefreshing(false);
                break;
            case TransferPresenter.TYPE_CHECK_ACL_TRANSFER_INFO:
                aclIsTrading = true;
                aclIsTradingDialog.show();
                break;
            case TransferPresenter.TYPE_CHECK_ETH_VALUE:
            case TransferPresenter.TYPE_CHECK_BTC_VALUE:
            case TransferPresenter.TYPE_CHECK_ACL_VALUE:
                break;

        }
    }

    @Override
    public void getDataFail(int errCode, String errMsg, int type) {
    }


    class TransferBroadcastReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(IntentFilterConstant.CHANGE_ASSETS_TYPE)) {
                LogUtils.log(className + " -- 收到货币更换的广播");
                if (!swipeRefreshLayout.isRefreshing()) {
                    swipeRefreshLayout.setRefreshing(true);
                }
                assetsDetailsItemEntity = (AssetsDetailsItemEntity) intent.getSerializableExtra("_assetsDetailsItemEntity");
                transferCoinName = assetsDetailsItemEntity.getAssetsName();
                LogUtils.log(className + " -- 收到广播对象的合约地址" + assetsDetailsItemEntity.getContractAddress());
                LogUtils.log(className + " -- 收到广播对象的名字" + transferCoinName);
                setData();
            }
        }
    }

    /**
     * 计算BTC FEE
     */
    private long getBtcFee() {
        if (utxoList == null) return -1;
        if (edt_transfer_amount.equals("")) return -1;

        if (ViewCommonUtils.getEdtString(edt_transfer_amount).equals("")) return -1;

        BigDecimal transferValueBD = new BigDecimal(ViewCommonUtils.getEdtString(edt_transfer_amount));
        long transferValue = transferValueBD.multiply(new BigDecimal(Math.pow(10, 8))).longValue();
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M) {
            utxoList.sort(new Comparator<UTXO>() {
                @Override
                public int compare(UTXO o1, UTXO o2) {
                    Long long1 = o1.getValue().getValue();
                    Long long2 = o2.getValue().getValue();
                    return long2.compareTo(long1);
                }
            });
        }

        int input = 0;
        long record = 0L;
        for (int i = 0; i < utxoList.size(); i++) {
            long utxoValue = utxoList.get(i).getValue().getValue();
            //加上未花费的值
            record += utxoValue;
            //需要的未花费数量+1
            input += 1;
            if (record > transferValue) {
                //未花费数量已经大于转账数量
                btcIsEnough = true;
                break;
            } else {
                //未花费数量小于或者等于转账数量
                btcIsEnough = false;
            }
        }
        if (!btcIsEnough) {
            LogUtils.logError(className + " -- btc 余额不足");
            return -1;
        }

        LogUtils.log(className + " -- btc 计算矿工费用 input = " + input);

        long fee = (input * 148 + 34 * 2 + 10) * btcGasPrice;

        return fee;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case Constant.PermissionsCode.CAMERA_PERMISSIONS_CODE:
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Intent intent = new Intent(this, MipcaActivityCapture.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivityForResult(intent, 1);
                } else {
                    LogUtils.log("相机权限申请失败");
                }
                break;
        }
    }

    @Override
    public void onNetChange(NetUtils.NetworkStatus status) {
        if (status == NetUtils.NetworkStatus.NETWORK_NONE) {
            tipsDialog.show();
            tv_next.setBackgroundResource(R.drawable.radius_btn_noenabled);
        } else {
            setData();
            String toAddress = ViewCommonUtils.getEdtString(edt_payment_address);
            String transferValue = ViewCommonUtils.getEdtString(edt_transfer_amount);
            if (!toAddress.equals("") && !transferValue.equals("")) {
                if (walletInfo.getChain() == Chain.BTC) {
                    if (AddressUtils.isBTCValidAddress(toAddress)) {
                        if (WalletApplication.getApp().isNetWork()) {
                            tv_next.setBackgroundResource(R.drawable.btn_collect_copy);
                        } else {
                            tv_next.setBackgroundResource(R.drawable.radius_btn_noenabled);
                        }
                    } else {
                        tv_next.setBackgroundResource(R.drawable.radius_btn_noenabled);
                    }
                } else {
                    if (AddressUtils.isETHValidAddress(toAddress)) {
                        if (WalletApplication.getApp().isNetWork()) {
                            tv_next.setBackgroundResource(R.drawable.btn_collect_copy);
                        } else {
                            tv_next.setBackgroundResource(R.drawable.radius_btn_noenabled);
                        }
                    } else {
                        tv_next.setBackgroundResource(R.drawable.radius_btn_noenabled);
                    }
                }
            } else {
                tv_next.setBackgroundResource(R.drawable.radius_btn_noenabled);
            }
        }
    }


}
