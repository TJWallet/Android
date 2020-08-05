package com.tianji.blockchain.activity.transfer;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.tianji.blockchainwallet.WalletManager;
import com.tianji.blockchainwallet.constant.enums.TransferType;
import com.tianji.blockchainwallet.entity.WalletInfo;
import com.tianji.blockchain.Constant;
import com.tianji.blockchain.IntentFilterConstant;
import com.tianji.blockchain.R;
import com.tianji.blockchain.WalletApplication;
import com.tianji.blockchain.activity.assets.TransferDetailsActivity;
import com.tianji.blockchain.activity.basic.BasicConnectShowActivity;
import com.tianji.blockchain.activity.main.AddressBookActivity;
import com.tianji.blockchain.activity.sacn.MipcaActivityCapture;
import com.tianji.blockchain.basic.BasicMvpInterface;
import com.tianji.blockchain.broadcast.NetChangeListener;
import com.tianji.blockchain.dialog.PasswordDialog;
import com.tianji.blockchain.dialog.TransferDetailsDialog;
import com.tianji.blockchain.entity.AssetsDetailsItemEntity;
import com.tianji.blockchain.entity.TransferRecode;
import com.tianji.blockchain.utils.CommonUtils;
import com.tianji.blockchain.utils.LogUtils;
import com.tianji.blockchain.utils.MathUtils;
import com.tianji.blockchain.utils.NetUtils;
import com.tianji.blockchain.utils.ViewCommonUtils;


import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;


public class TransferActivity extends BasicConnectShowActivity implements View.OnClickListener, BasicMvpInterface, NetChangeListener {
    private static final int TYPE_FILECOIN_SIGN_SUCCESS = 0;
    private static final int TYPE_FILECOIN_SIGN_FILE = 1;
    private static final int GET_GASLIMIT = 2;
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
    private BigDecimal gasPriceOnline = new BigDecimal(1000000L);
    private long gasLimit = -1L;
    private long nonce;
    private BigDecimal gasPriceParams;
    private double blance;
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

    private String toAddress;

    private int speed = 0;

    private RelativeLayout root_view;


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
        presenter.checkFilecoinBalance(walletInfo.getAddress());
        presenter.checkFilecoinNonce(walletInfo.getAddress());
        presenter.checkFilecoinGasprice();
    }

    @Override
    protected void handleMsg(Message msg) {
        switch (msg.what) {
            case GET_GASLIMIT:
                swipeRefreshLayout.setRefreshing(true);
                String transferValue = ViewCommonUtils.getEdtString(edt_transfer_amount);
                String toAddress = CommonUtils.replaceEnterAndSpace(ViewCommonUtils.getEdtString(edt_payment_address));
                BigInteger valueBigInteger = new BigDecimal(transferValue).multiply(new BigDecimal(Math.pow(10, 18))).toBigInteger();
                LogUtils.log("需要转账的金额是 =" + valueBigInteger);
                presenter.checkFilecoinGaslimit(walletInfo.getAddress(), Integer.parseInt(nonce + ""), toAddress, valueBigInteger.toString());
                break;
            case TYPE_FILECOIN_SIGN_SUCCESS:
                String signResult = (String) msg.obj;
                LogUtils.log("签名成功 =" + signResult);
                Map<String, String> params = new HashMap<>();
                params.put("json", signResult);
                presenter.filecoinTransfer(params);
                break;
        }
    }

    @Override
    protected void initReceiver() {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
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
        tv_mining_fee_value = findViewById(R.id.tv_mining_fee_value);
        tv_next = findViewById(R.id.tv_next);
        swipeRefreshLayout = findViewById(R.id.swipeRefreshLayout);
        //2.0转账
        tv_chain_type = findViewById(R.id.tv_chain_type);
        tv_wallet_address = findViewById(R.id.tv_wallet_address);
        tv_chain_name = findViewById(R.id.tv_chain_name);
        tv_amount = findViewById(R.id.tv_amount);
        root_view = findViewById(R.id.root_view);
        tv_assets_name = findViewById(R.id.tv_assets_name);

        tv_wallet_address.setText(walletInfo.getAddress());
        tv_chain_type.setText(walletInfo.getWalletName());
        tv_assets_name.setText(assetsDetailsItemEntity.getAssetsName());
        swipeRefreshLayout.setEnabled(false);
        swipeRefreshLayout.setRefreshing(true);
        img_icon.setImageResource(R.drawable.file_coin_select);
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
                mHandler.removeMessages(GET_GASLIMIT);
                String strValue = s.toString();
                String toAddress = CommonUtils.replaceEnterAndSpace(ViewCommonUtils.getEdtString(edt_payment_address));
                String transferValue = s.toString();
                if (!toAddress.equals("") && !transferValue.equals("")) {
                    if (WalletApplication.getApp().isNetWork()) {
                        tv_next.setBackgroundResource(R.drawable.btn_collect_copy);
                    } else {
                        tv_next.setBackgroundResource(R.drawable.radius_btn_noenabled);
                    }
                    mHandler.sendEmptyMessageDelayed(GET_GASLIMIT, 1000);
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
                    if (WalletApplication.getApp().isNetWork()) {
                        tv_next.setBackgroundResource(R.drawable.btn_collect_copy);
                    } else {
                        tv_next.setBackgroundResource(R.drawable.radius_btn_noenabled);
                    }
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
                    String value = ViewCommonUtils.getEdtString(edt_transfer_amount);
                    if (event.getAction() == MotionEvent.ACTION_UP) {
                        if (pro < 100) {
                            pro += 50;
                        } else {
                            pro = 0;
                        }
                        seekbar.setProgress(pro);
                        if (seekbar.getProgress() == 0) {
                            //慢
                            speed = 0;
                            if (gasLimit != -1) {
                                gasPrice = gasPriceOnline.multiply(new BigDecimal(Constant.GasPriceSpeed.TYPE_SPEED_SLOW));
                                gasPriceParams = gasPrice.divide(new BigDecimal(Math.pow(10, 9)));
                                tv_mining_fee_value.setText(MathUtils.getMinersFee(gasPrice, new BigDecimal(gasLimit), speed) + " FIL");
                            }

                        } else if (seekbar.getProgress() == 50) {
                            //中
                            speed = 1;
                            if (gasLimit != -1) {
                                gasPrice = gasPriceOnline.multiply(new BigDecimal(Constant.GasPriceSpeed.TYPE_SPEED_NORMAL));
                                gasPriceParams = gasPrice.divide(new BigDecimal(Math.pow(10, 9)));
                                tv_mining_fee_value.setText(MathUtils.getMinersFee(gasPrice, new BigDecimal(gasLimit), speed) + " FIL");
                            }
                        } else if (seekbar.getProgress() == 100) {
                            //快
                            speed = 2;
                            if (gasLimit != -1) {
                                gasPrice = gasPriceOnline.multiply(new BigDecimal(Constant.GasPriceSpeed.TYPE_SPEED_FAST));
                                gasPriceParams = gasPrice.divide(new BigDecimal(Math.pow(10, 9)));
                                tv_mining_fee_value.setText(MathUtils.getMinersFee(gasPrice, new BigDecimal(gasLimit), speed) + " FIL");
                            }
                        }
                    }
                }
                return true;
            }
        });

        rl_address_book.setOnClickListener(this);
        rl_scan.setOnClickListener(this);
        tv_next.setOnClickListener(this);
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
                if (!pwd.equals("")) {
                    try {
                        startTransfer(pwd, transferValue, toAddress, walletInfo);
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
     */
    private void startTransfer(String pwd, String transferValue, String toAddress, WalletInfo walletInfo) {
        if (passwordDialog.isShowing()) {
            passwordDialog.dismiss();
        }
        swipeRefreshLayout.setRefreshing(true);
        LogUtils.log("FILECOIN交易签名");
        filecoinSign(pwd, transferValue, toAddress, walletInfo);
    }

    /***
     * Filecoin交易签名（T1）
     */
    public void filecoinSign(String pwd, String transferValue, String toAddress, WalletInfo walletInfo) {

        Map<String, Object> params = new HashMap<>();
        params.put("transferType", TransferType.FILECOIN_T1);
        params.put("to", toAddress);
        params.put("nonce", nonce);
        params.put("gasPrice", gasPriceParams.toBigInteger());
        params.put("gasLimit", gasLimit);
        params.put("value", new BigDecimal(transferValue).multiply(new BigDecimal(Math.pow(10, 18))).toBigInteger());

        WalletManager.getInstance().signTransaction(this, walletInfo, pwd, params, (resultCode, result) -> {
            switch (resultCode) {
                case SUCCESS:
                    Message message = Message.obtain();
                    message.what = TYPE_FILECOIN_SIGN_SUCCESS;
                    message.obj = result;
                    mHandler.sendMessage(message);
                    break;
                case FAIL:
                    Message failMessage = Message.obtain();
                    failMessage.what = TYPE_FILECOIN_SIGN_FILE;
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
                long valueLong = new BigDecimal(transferValue).multiply(new BigDecimal(Math.pow(10, 18))).longValue();
                if (!toAddress.equals("")) {
                    if (!transferValue.equals("")) {
                        if (gasLimit != -1) {
                            feeCompareBlance(blance, "FIL", transferValue, toAddress);
                        } else {
                            LogUtils.log("gasLimit == -1");
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
    private void feeCompareBlance(double blance, String coinName, String transferValue, String toAddress) {
        String tips = String.format(getResources().getString(R.string.money_not_enough), "FIL");
        String fee = tv_mining_fee_value.getText().toString().replace(coinName, "").trim();
        LogUtils.log(className + " -- 矿工费是str =" + fee);
        try {
            double feeDouble = Double.parseDouble(fee);
            double value = Double.parseDouble(transferValue.trim());
            LogUtils.log(className + " -- 矿工费是 =" + feeDouble);
            LogUtils.log(className + " -- 转账金额是 =" + value);
            LogUtils.log(className + " -- 总需求金额是 =" + (feeDouble + value));
            LogUtils.log(className + " -- 余额是 =" + blance);
            if ((feeDouble + value) < blance) {
                showTransferDetails(transferValue, toAddress);
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
     * @param transferValue
     * @param toAddress
     */
    private void showTransferDetails(String transferValue, String toAddress) {
        LogUtils.log(className + " -- showTransferDetails toaddress == " + toAddress);
        transferRecode.setTo(toAddress);
        transferRecode.setFrom(walletInfo.getAddress());

        transferRecode.setTokenDecimal("18");
        transferRecode.setValue(new BigDecimal(transferValue).multiply(new BigDecimal(Math.pow(10, 18))).toString());
        transferRecode.setCumulativeGasUsed(MathUtils.getMinersFeeBigDecimal(gasPrice, new BigDecimal(gasLimit), speed) + "");

        transferRecode.setGasUsed(gasLimit + "");
        switch (speed) {
            case 0:
                gasPrice = gasPriceOnline.multiply(new BigDecimal(Constant.GasPriceSpeed.TYPE_SPEED_SLOW));
                break;
            case 1:
                gasPrice = gasPriceOnline.multiply(new BigDecimal(Constant.GasPriceSpeed.TYPE_SPEED_NORMAL));
                break;
            case 2:
                gasPrice = gasPriceOnline.multiply(new BigDecimal(Constant.GasPriceSpeed.TYPE_SPEED_FAST));
                break;
        }
        transferRecode.setGasPrice(gasPrice + "");
        transferRecode.setSpeed(speed);

        transferDetailsDialog = new TransferDetailsDialog(this, R.style.Wallet_Manager_Dialog, transferCoinName, transferRecode, walletInfo, this);
        if (!toAddress.equals("")) {
            if (!transferValue.equals("")) {
                if (Double.parseDouble(transferValue) > amount) {
                    String tips = String.format(getResources().getString(R.string.money_not_enough), assetsDetailsItemEntity.getAssetsName());
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
            case TransferPresenter.CHECK_FILECOIN_BALANCE:
                String doubleString = (String) object;
                amount = Double.parseDouble(doubleString);
                blance = amount;
                tv_amount.setText(MathUtils.doubleKeep4(amount));
                tv_chain_name.setText(transferCoinName);
                img_icon.setImageResource(R.drawable.file_coin_select);
                break;
            case TransferPresenter.CHECK_FILECOIN_NONCE:
                LogUtils.log("请求到的nonce=" + object);
                String strNonce = (String) object;
                nonce = Long.parseLong(strNonce);
                break;
            case TransferPresenter.CHECK_FILECOIN_GASPRICE:
                LogUtils.log("请求到的gasprice=" + object);
                String str = (String) object;
                gasPriceOnline = new BigDecimal(str);
                switch (speed) {
                    case 0:
                        gasPrice = gasPriceOnline.multiply(new BigDecimal(Constant.GasPriceSpeed.TYPE_SPEED_SLOW));
                        break;
                    case 1:
                        gasPrice = gasPriceOnline.multiply(new BigDecimal(Constant.GasPriceSpeed.TYPE_SPEED_NORMAL));
                        break;
                    case 2:
                        gasPrice = gasPriceOnline.multiply(new BigDecimal(Constant.GasPriceSpeed.TYPE_SPEED_FAST));
                        break;
                }
                gasPriceParams = gasPrice.divide(new BigDecimal(Math.pow(10, 9)));

                if (swipeRefreshLayout.isRefreshing()) {
                    swipeRefreshLayout.setRefreshing(false);
                }

                break;
            case TransferPresenter.CHECK_FILECOIN_GASLIMIT:
                LogUtils.log("请求到的gasLimit=" + object);
                String strLimit = (String) object;
                gasLimit = Long.parseLong(strLimit);
                if (gasLimit != -1) {
                    switch (speed) {
                        case 0:
                            gasPrice = gasPriceOnline.multiply(new BigDecimal(Constant.GasPriceSpeed.TYPE_SPEED_SLOW));
                            break;
                        case 1:
                            gasPrice = gasPriceOnline.multiply(new BigDecimal(Constant.GasPriceSpeed.TYPE_SPEED_NORMAL));
                            break;
                        case 2:
                            gasPrice = gasPriceOnline.multiply(new BigDecimal(Constant.GasPriceSpeed.TYPE_SPEED_FAST));
                            break;
                    }
                    gasPriceParams = gasPrice.divide(new BigDecimal(Math.pow(10, 9)));
                    tv_mining_fee_value.setText(MathUtils.getMinersFee(gasPrice, new BigDecimal(gasLimit), speed) + " FIL");
                }
                if (swipeRefreshLayout.isRefreshing()) {
                    swipeRefreshLayout.setRefreshing(false);
                }
                break;
            case TransferPresenter.FILECOIN_START_TRANSFER:
                String hash = (String) object;
                try {
                    JSONObject rootObj = new JSONObject(hash);
                    String myHash = rootObj.optString("/");
                    transferRecode.setHash(myHash);
                    transferRecode.setGas(gasLimit + "");
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
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                break;

            default:
                throw new IllegalStateException("Unexpected value: " + type);
        }
    }

    @Override
    public void getDataFail(Object error, int type) {
        switch (type) {

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
                if (WalletApplication.getApp().isNetWork()) {
                    tv_next.setBackgroundResource(R.drawable.btn_collect_copy);
                } else {
                    tv_next.setBackgroundResource(R.drawable.radius_btn_noenabled);
                }
            } else {
                tv_next.setBackgroundResource(R.drawable.radius_btn_noenabled);
            }
        }
    }


}
